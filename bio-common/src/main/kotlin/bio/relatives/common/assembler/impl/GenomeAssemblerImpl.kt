package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.parser.FeatureParser
import bio.relatives.common.processor.AssemblyProcessor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class GenomeAssemblerImpl(
    /**
     * Assembling context, which contains singletons and some additional data,
     * required for the assembling. Will be used on lower levels of the assembling.
     */
    private val assembleCtx: AssemblyCtx,

    /**
     * Parser, which will be used to extract features from the feature file.
     */
    private val featureParser: FeatureParser
) : GenomeAssembler {

    /**
     * Parent scope for the execution of all the assembling coroutines.
     */
    private val scope = CoroutineScope(
        SupervisorJob() +
            CoroutineName("Assembler") +
            Executors.newFixedThreadPool(DEFAULT_ASSEMBLY_THREADS).asCoroutineDispatcher() +
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
    private val processor = AssemblyProcessor(assembleCtx, scope)

    override fun assemble(): ReceiveChannel<RegionBatch> =
        processor.outputChannel.also {
            scope.launch {
                val features = featureParser.parseFeatures(assembleCtx.featureFilePath)
                for (feature in features) {
                    processor.inputChannel.send(feature)
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
        val LOG: Logger = LoggerFactory.getLogger(GenomeAssemblerImpl::class.java)

        const val DEFAULT_ASSEMBLY_THREADS = 5
    }
}