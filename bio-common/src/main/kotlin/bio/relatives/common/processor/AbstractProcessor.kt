package bio.relatives.common.processor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID
import java.util.concurrent.ThreadPoolExecutor

/**
 * TODO: tests!
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class AbstractProcessor<P : Any, R : Any>(
    coroutineUniqueIdentifier: UUID = UUID.randomUUID(),
    coroutineThreadPool: ThreadPoolExecutor? = null,
    processingTimeout: Long = DEFAULT_PROCESS_TIMEOUT
) : AutoCloseable {
    /**
     * Inner channel, which is used to transfer the output of the sub-coroutines
     * to external consumers. Exposed to external consumer as [ReceiveChannel].
     */
    private val _outputChannel: Channel<R> = Channel(OUTPUT_CHANNEL_CAPACITY)
    val outputChannel: ReceiveChannel<R> get() = _outputChannel

    /**
     * Inner channel, which is used to transfer input data from external producers in
     * order to process them. Exposed to external producers as [SendChannel].
     */
    private val _inputChannel: Channel<P> = Channel(INPUT_CHANNEL_CAPACITY)
    val inputChannel: SendChannel<P> get() = _inputChannel

    /**
     * Coroutine scope, used by the dispatcher. Uses the provided
     * thread pool as [CoroutineDispatcher]. Specifies the name of the
     * associated coroutine and exception handler, which logs the exceptions by default.
     */
    private val scope = CoroutineScope(
        CoroutineName("Processor-${coroutineUniqueIdentifier}") +
            (coroutineThreadPool?.asCoroutineDispatcher() ?: Dispatchers.Default) +
            CoroutineExceptionHandler { ctx, exception ->
                LOG.error(
                    "Exception occurred while processing data in ${ctx[CoroutineName.Key]}:",
                    exception
                )
            }
    )

    /**
     * Coroutine, whose main purpose is to check the [inputChannel] and dispatch
     * the messages into [dispatcher] coroutine.
     */
    private val poller = scope.launch {
        _inputChannel.consumeEach {
            dispatcher.send(
                ProcessorAction.ProcessPayload(it, processingTimeout)
            )
        }
    }

    /**
     * Actor which is responsible for the dispatching of the incoming tasks to sub-processors.
     * Maintains a list of [AbstractSubProcessor] sub-coroutines, that are responsible for
     * the processing of the incoming tasks.
     */
    private val dispatcher = scope.actor<ProcessorAction>(
        capacity = DISPATCHER_CHANNEL_CAPACITY,
        start = CoroutineStart.LAZY
    ) {
        val children: MutableList<AbstractSubProcessor> = mutableListOf()
        consumeEach { action ->
            when (action) {
                is ProcessorAction.ProcessPayload -> dispatch(children, action)
                is ProcessorAction.CloseSubProcessors -> children.forEach { it.close() }
            }
        }
    }

    /**
     * Closes the channels used by the implementation. Firstly dispatcher, then
     * channels associated with sub-routines.
     */
    override fun close() = runBlocking<Unit> {
        // close the poller
        poller.cancel()

        // close channels
        _inputChannel.close()
        _outputChannel.close()

        // close the dispatcher and all sub-routines
        dispatcher.send(ProcessorAction.CloseSubProcessors)
        dispatcher.close()
    }

    /**
     * Dispatches [action] to one of the sub-processors, using the following algorithm:
     * - chooses one of the idle SP with the min number of processed tasks
     * - if no idle present && we are able to create a new SP - do so
     * - otherwise wait [DISPATCH_WAITING_DELAY_MS] for some SP to free
     * - retry for [MAX_DISPATCH_RETRIES_NUM] or throw exception
     */
    private suspend fun dispatch(
        children: MutableList<AbstractSubProcessor>,
        action: ProcessorAction.ProcessPayload
    ) {
        for (retry in 0 until MAX_DISPATCH_RETRIES_NUM) {
            var chosenChild = children
                .filter { it.idle }
                .minByOrNull { it.processed }

            if (chosenChild == null) {
                if (children.size < MAX_CHILDREN_NUM) {
                    chosenChild = makeSubProcessor()
                    children.add(chosenChild)
                } else {
                    delay(DISPATCH_WAITING_DELAY_MS)
                    continue
                }
            }

            chosenChild.dispatch(action)
        }

        throw IllegalStateException("Unable to dispatch an assembly task - out of retries!")
    }

    /**
     * Used to create instances of [AbstractSubProcessor], that are defined in sub-classes
     * and specify the processing logic.
     */
    protected abstract fun makeSubProcessor(): AbstractSubProcessor

    /**
     * Sub processor, which performs the tasks, provided to the processor itself.
     * [idle] determines whether this processor is free or not, while
     * [processed] is a number of tasks, already completed by this processor.
     */
    protected abstract inner class AbstractSubProcessor : AutoCloseable {
        private var _idle: Boolean = true
        val idle: Boolean get() = _idle

        private var _processed: Long = 0L
        val processed: Long get() = _processed

        /**
         * Actor which is responsible for the processing of the incoming tasks.
         */
        private val processor = scope.actor<ProcessorAction>(
            capacity = CHILD_CHANNEL_CAPACITY,
            start = CoroutineStart.LAZY
        ) {
            consumeEach {
                when (it) {
                    is ProcessorAction.ProcessPayload -> {
                        _idle = false

                        val (payload, timeout) = it.unpack<P>()
                        withTimeout(timeout) {
                            _outputChannel.send(
                                process(this, payload)
                            )
                        }

                        _idle = true
                        _processed += 1
                    }
                    // sub-processors must be closed only by calling close() method
                    else -> throw UnsupportedOperationException()
                }
            }
        }

        /**
         * Contains the processing logic of the provided [payload] in [parentScope]
         * of the parent actor.
         */
        protected abstract suspend fun process(parentScope: CoroutineScope, payload: P): R

        /**
         * Internal processing function, used to put [action] into the queue.
         */
        suspend fun dispatch(action: ProcessorAction.ProcessPayload) {
            processor.send(action)
        }

        /**
         * Closes the actor. The only way to do so.
         */
        override fun close() {
            processor.close()
        }
    }

    /**
     * Sealed class hierarchy, which represent the actions, supported by
     * [AbstractSubProcessor] and [AbstractProcessor].
     */
    sealed class ProcessorAction {
        /**
         * Process the provided [payload] with specified [timeout].
         */
        class ProcessPayload(
            private val payload: Any?,
            private val timeout: Long
        ) : ProcessorAction() {
            @Suppress("unchecked_cast")
            fun <T> unpack(): Pair<T, Long> {
                return (payload as T) to timeout
            }
        }

        /**
         * Close the handles of all the allocated resources (actors, channels, etc).
         */
        object CloseSubProcessors : ProcessorAction()
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractProcessor::class.java)

        const val MAX_CHILDREN_NUM = 10

        const val MAX_DISPATCH_RETRIES_NUM = 10

        const val DISPATCH_WAITING_DELAY_MS = 1000L

        const val DISPATCHER_CHANNEL_CAPACITY = 10

        const val CHILD_CHANNEL_CAPACITY = 10

        const val OUTPUT_CHANNEL_CAPACITY = 10

        const val INPUT_CHANNEL_CAPACITY = 10

        const val DEFAULT_PROCESS_TIMEOUT = 10000L
    }
}