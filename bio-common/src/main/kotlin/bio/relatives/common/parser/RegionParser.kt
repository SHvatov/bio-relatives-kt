package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import htsjdk.samtools.SAMRecord
import java.io.Closeable

/**
 * @author shvatov
 */
interface RegionParser : Closeable {
    /**
     * From the BAM file this parser is associated with parses the [SAMRecord] instances,
     * that contains the provided [feature].
     */
    fun parseRegion(feature: Feature): List<SAMRecord>
}