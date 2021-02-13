package bio.relatives.common.comparator.impl

import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.RegionBatch
import bio.relatives.common.processor.CoroutineScopeAware.DefaultExceptionHandlerProvider.createLoggingExceptionHandler
import bio.relatives.common.processor.impl.CompareProcessor
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class GenomeComparatorImpl(
    /**
     * Comparison context, which contains singletons and some additional data,
     * required for the proper work of the comparison algorithms on lower levels.
     */
    compareCtx: CompareCtx,

    /**
     * Input channel, which will be used to fetch the data from the assembler.
     */
    private val inputChannel: ReceiveChannel<RegionBatch>,

    /**
     * Coroutine scope of the parent coroutine.
     */
    override val parentScope: CoroutineScope
) : GenomeComparator {

    /**
     * Parent scope for the execution of all the assembling coroutines.
     */
    override val scope = CoroutineScope(
        parentScope.coroutineContext +
            CoroutineName("Comparator") +
            createLoggingExceptionHandler(LOG)
    )

    /**
     * Processor, which represents a separate coroutine, which will be used
     * for the assembling of the genome.
     */
    private val processor = CompareProcessor(compareCtx, scope)

    override fun compare(): ReceiveChannel<ComparisonResult> =
        processor.outputChannel.also {
            scope.launch {
                inputChannel.consumeEach {
                    processor.inputChannel.send(it)
                }
            }
        }

    override fun close() {
        // close the processor
        processor.close()

        // cancel the scope
        scope.cancel()
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(GenomeComparatorImpl::class.java)
    }
}