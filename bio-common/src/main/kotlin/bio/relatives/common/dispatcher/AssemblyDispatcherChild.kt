package bio.relatives.common.dispatcher

import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.model.Feature

/**
 * @author shvatov
 */
internal interface AssemblyDispatcherChild {
    fun process(feature: Feature, onComplete: suspend (RegionBatch) -> Unit)
}