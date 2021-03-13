package bio.relatives.common.assembler

import bio.relatives.common.model.RegionBatch

/**
 * @author shvatov
 */
interface GenomeAssembler {
    fun assemble(): List<RegionBatch>
}