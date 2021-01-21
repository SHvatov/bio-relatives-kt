package bio.relatives.common.assembler

import bio.relatives.common.model.Feature
import bio.relatives.common.model.PersonAware
import bio.relatives.common.model.Region
import htsjdk.samtools.SAMRecord

/**
 * @author shvatov
 */
interface RegionAssembler : PersonAware {
    fun assemble(feature: Feature, records: List<SAMRecord>): Region
}