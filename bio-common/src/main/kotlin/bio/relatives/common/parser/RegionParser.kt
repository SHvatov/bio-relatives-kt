package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import htsjdk.samtools.SAMRecord
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface RegionParser : AutoCloseable {
    fun parseRegion(feature: Feature): ReceiveChannel<SAMRecord>
}