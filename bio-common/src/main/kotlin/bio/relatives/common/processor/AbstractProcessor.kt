package bio.relatives.common.processor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.UUID

/**
 * TODO: maybe extract into an interface?
 * TODO: TESTS!
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class AbstractProcessor<P : Any, R : Any>(
    private val inputChannel: ReceiveChannel<P>
) : AutoCloseable {
    /**
     * List of [SubProcessor] sub-coroutines, that are responsible for
     * the processing of the incoming tasks.
     */
    private val children: MutableList<SubProcessor> = mutableListOf()

    /**
     * Channel, which is used to transfer the output of the sub-coroutines
     * to external consumers.
     */
    private val outputChannel: Channel<R> = Channel(OUTPUT_CHANNEL_CAPACITY)

    /**
     * Coroutine scope, used by the dispatcher. Uses the provided
     * thread pool as [CoroutineDispatcher]. Specifies the name of the
     * associated coroutine and exception handler, which logs the excpetions by default.
     *
     * TODO: provide thread pool
     * TODO: log occurring exceptions in exception handler
     */
    private val dispatcherScope = CoroutineScope(
        CoroutineName("Dispatcher-${UUID.randomUUID()}") +
            Dispatchers.Default +
            CoroutineExceptionHandler { _, _ -> Unit }
    )

    /**
     * Actor which is responsible for the dispatching of the incoming tasks to sub-processors.
     */
    private val dispatcher = dispatcherScope.actor<ProcessorAction>(
        capacity = DISPATCHER_CHANNEL_CAPACITY
    ) {
        consumeEach { dispatch(it) }
    }

    /**
     * Starts the processing of the data from [inputChannel] provided in the constructor
     * in a separate coroutine. Returns an [outputChannel], which contains the results of the
     * processing.
     */
    fun process(timeout: Long? = null): ReceiveChannel<R> {
        dispatcherScope.launch {
            inputChannel.consumeEach {
                dispatcher.send(
                    ProcessorAction(it, timeout)
                )
            }
        }
        return outputChannel
    }

    /**
     * Closes the channels used by the implementation. Firstly dispatcher, then
     * channels associated with sub-routines.
     */
    override fun close() {
        dispatcher.close()
        for (child in children) child.close()
    }

    /**
     * Dispatches [action] to one of the sub-processors, using the following algorithm:
     * - chooses one of the idle SP with the min number of processed tasks
     * - if no idle present && we are able to create a new SP - do so
     * - otherwise wait [DISPATCH_WAITING_DELAY_MS] for some SP to free
     * - retry for [MAX_DISPATCH_RETRIES_NUM] or throw exception
     */
    private suspend fun dispatch(action: ProcessorAction) {
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

            chosenChild.process(action)
        }

        throw IllegalStateException("Unable to dispatch an assembly task - out of retries!")
    }

    protected abstract fun makeSubProcessor(): SubProcessor

    /**
     * Sub processor, which performs the tasks, provided to the processor itself.
     * [idle] determines whether this processor is free or not, while
     * [processed] is a number of tasks, already completed by this processor.
     */
    protected abstract inner class SubProcessor : AutoCloseable {
        private var _idle: Boolean = true
        val idle: Boolean get() = _idle

        private var _processed: Long = 0L
        val processed: Long get() = _processed

        private val processor = dispatcherScope.actor<ProcessorAction>(
            capacity = CHILD_CHANNEL_CAPACITY
        ) {
            consumeEach {
                _idle = false

                val (payload, timeout) = it.unpack<P>()
                withTimeout(timeout) {
                    outputChannel.send(
                        process(
                            payload
                        )
                    )
                }

                _idle = true
                _processed += 1
            }
        }

        protected abstract suspend fun process(payload: P): R

        internal suspend fun process(action: ProcessorAction) = processor.send(action)

        override fun close() {
            processor.close()
        }
    }

    /**
     * Hierarchy of sealed classes, that represent the actions, supported by [SubProcessor]
     * and [AbstractProcessor]. Processes the incoming [payload] and puts the
     * result into [outputChannel].
     */
    internal class ProcessorAction(
        private val payload: Any?,
        private val timeout: Long? = null
    ) {
        @Suppress("unchecked_cast")
        fun <T> unpack(): Pair<T, Long> {
            return (payload as T) to (timeout ?: DEFAULT_PROCESS_TIMEOUT)
        }
    }

    private companion object {
        const val MAX_CHILDREN_NUM = 10

        const val MAX_DISPATCH_RETRIES_NUM = 10

        const val DISPATCH_WAITING_DELAY_MS = 1000L

        const val DISPATCHER_CHANNEL_CAPACITY = 10

        const val CHILD_CHANNEL_CAPACITY = 10

        const val OUTPUT_CHANNEL_CAPACITY = 10

        const val DEFAULT_PROCESS_TIMEOUT = 10000L
    }
}