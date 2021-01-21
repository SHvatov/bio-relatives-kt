package bio.relatives.common.processor

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.model.Feature
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AssemblyProcessor(
    private val ctx: AssemblyCtx,
    inputChannel: ReceiveChannel<Feature>
) : AbstractProcessor<Feature, RegionBatch>(inputChannel) {
    override fun makeSubProcessor() = object : SubProcessor() {
        override suspend fun process(payload: Feature): RegionBatch {
            TODO("Add implementation")
        }
    }
}