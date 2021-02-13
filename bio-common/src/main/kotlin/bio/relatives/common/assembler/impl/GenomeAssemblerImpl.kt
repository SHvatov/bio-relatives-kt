package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.model.RegionBatch
import bio.relatives.common.processor.CoroutineScopeAware.DefaultExceptionHandlerProvider.createLoggingExceptionHandler
import bio.relatives.common.processor.impl.AssemblyProcessor
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
     * Coroutine scope of the parent coroutine.
     */
    override val parentScope: CoroutineScope
) : GenomeAssembler {

    /**
     * Parent scope for the execution of all the assembling coroutines.
     */
    override val scope = CoroutineScope(
        parentScope.coroutineContext +
            CoroutineName("Assembler") +
            createLoggingExceptionHandler(LOG)
    )

    /**
     * Processor, which represents a separate coroutine, which will be used
     * for the assembling of the genome.
     */
    private val processor = AssemblyProcessor(assembleCtx, scope)

    override fun assemble(): ReceiveChannel<RegionBatch> =
        processor.outputChannel.also {
            scope.launch {
                val features = assembleCtx
                    .featureParser
                    .parseFeatures(assembleCtx.featureFilePath)

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
    }
}