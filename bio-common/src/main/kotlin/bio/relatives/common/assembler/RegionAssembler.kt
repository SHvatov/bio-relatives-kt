package bio.relatives.common.assembler

import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region

/**
 * @author shvatov
 */
interface RegionAssembler : AutoCloseable {
    fun assemble(feature: Feature): Region
}