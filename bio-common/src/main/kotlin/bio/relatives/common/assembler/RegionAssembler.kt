package bio.relatives.common.assembler

import bio.relatives.common.model.Region
import htsjdk.samtools.SAMRecord

/**
 * @author shvatov
 */
interface RegionAssembler : AutoCloseable {
    fun assemble(records: List<SAMRecord>): Region
}