package bio.relatives.common.comparator.impl

import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.processor.CompareProcessor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

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
    private val inputChannel: ReceiveChannel<RegionBatch>
) : GenomeComparator {

    /**
     * Parent scope for the execution of all the assembling coroutines.
     */
    private val scope = CoroutineScope(
        SupervisorJob() +
            CoroutineName("Comparator") +
            Executors.newFixedThreadPool(DEFAULT_CMP_THREADS).asCoroutineDispatcher() +
            CoroutineExceptionHandler { ctx, exception ->
                LOG.error(
                    "Exception occurred while processing data in ${ctx[CoroutineName]}:",
                    exception
                )
            }
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

        const val DEFAULT_CMP_THREADS = 3
    }
}