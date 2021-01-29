package bio.relatives.common.assembler

import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware.Role
import htsjdk.samtools.SAMRecord

/**
 * @author shvatov
 */
interface RegionAssembler {
    fun assemble(role: Role, feature: Feature, records: List<SAMRecord>): Region
}