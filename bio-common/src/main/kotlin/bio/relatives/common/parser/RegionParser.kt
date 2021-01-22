package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import htsjdk.samtools.SAMRecord

/**
 * @author shvatov
 */
interface RegionParser : AutoCloseable {
    fun parseRegion(feature: Feature): List<SAMRecord>
}