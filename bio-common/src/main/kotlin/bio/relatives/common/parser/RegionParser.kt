package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import bio.relatives.common.model.PersonAware
import htsjdk.samtools.SAMRecord

/**
 * @author shvatov
 */
interface RegionParser : PersonAware, AutoCloseable {
    fun parseRegion(feature: Feature): List<SAMRecord>
}