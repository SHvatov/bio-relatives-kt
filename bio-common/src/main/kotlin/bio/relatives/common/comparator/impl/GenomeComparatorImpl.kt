package bio.relatives.common.comparator.impl

import bio.relatives.common.assembler.GenomeAssemblyResult
import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.comparator.GenomeComparisonResult
import bio.relatives.common.comparator.GenomeComparisonTask
import bio.relatives.common.model.ComparisonParticipants
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.RegionBatch
import com.shvatov.processor.CoroutineScopeAware
import com.shvatov.processor.config.TaskProcessorConfiguration
import com.shvatov.processor.impl.TaskProcessorImpl
import com.shvatov.processor.use
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import kotlin.coroutines.EmptyCoroutineContext

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
    private val compareCtx: CompareCtx,

    /**
     * Input channel, that will contain the results of the genome assembly
     * step of the algorithm.
     */
    override val inputChannel: ReceiveChannel<GenomeAssemblyResult>,

    /**
     * Scope of the parent coroutine this assembler is called from.
     */
    override val parentScope: CoroutineScope? = null
) : GenomeComparator {

    /**
     * Parent scope for the execution of all the assembling coroutines.
     */
    override val scope = CoroutineScope(
        (parentScope?.coroutineContext ?: EmptyCoroutineContext) +
            CoroutineName("Comparator") +
            Executors.newFixedThreadPool(DEFAULT_CMP_THREADS).asCoroutineDispatcher() +
            CoroutineScopeAware.exceptionHandler(LOG)
    )

    /**
     * Processor, which represents a separate coroutine, which will be used
     * for the assembling of the genome.
     */
    private val processor = TaskProcessorImpl<RegionBatch, ComparisonResult>(
        PROCESSOR_CONFIGURATION,
        scope
    )

    override fun compare(): ReceiveChannel<GenomeComparisonResult> =
        processor.outputChannel.also {
            scope.launch {
                processor.use {
                    inputChannel.consumeEach { assemblyResult ->
                        if (assemblyResult.isSuccess) {
                            submit(
                                GenomeComparisonTask(assemblyResult.result!!) {
                                    compareRegions(it)
                                }
                            )
                        }
                    }
                }
            }
        }

    /**
     * Performs synchronized comparison of the regions from [regionBatch].
     */
    private fun compareRegions(regionBatch: RegionBatch): ComparisonResult {
        return ComparisonParticipants.COMPARISON_PAIRS
            .associateWith { regionBatch.regionsByRole[it.firstRole] to regionBatch.regionsByRole[it.secondRole] }
            .filter { (_, value) -> value.first != null && value.second != null }
            .mapValues { (_, value) ->
                compareCtx.comparisonMethod.compare(
                    regionBatch.feature,
                    value.first!!,
                    value.second!!
                )
            }.toMap(ComparisonResult())
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(GenomeComparatorImpl::class.java)

        /**
         * Number of threads to be used by the assembler coroutine dispatcher.
         */
        const val DEFAULT_CMP_THREADS = 3

        /**
         * Default [TaskProcessorImpl] configuration.
         */
        val PROCESSOR_CONFIGURATION = TaskProcessorConfiguration(
            exceptionHandler = CoroutineScopeAware.exceptionHandler(LOG),
        )
    }
}