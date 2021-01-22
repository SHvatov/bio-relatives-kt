package bio.relatives.common.parser.impl

import bio.relatives.common.model.Feature
import bio.relatives.common.parser.RegionParser
import bio.relatives.common.utils.isValid
import htsjdk.samtools.SAMRecord
import htsjdk.samtools.SamReader
import htsjdk.samtools.SamReaderFactory
import htsjdk.samtools.ValidationStringency
import java.nio.file.Path

/**
 * Naive implementation of [RegionParser], which uses [SamReader] to parse
 * the [SAMRecord] instances from BAM file it is associated with.
 * [SamReader] is created using the [SamReaderFactory] provided from the outer scope.
 * @author shvatov
 */
class RegionParserImpl(
    bamFilePath: Path,
    samReaderFactory: SamReaderFactory
) : RegionParser {
    init {
        if (!bamFilePath.isValid(requiredExtension = BAM_FILE_EXTENSION)) {
            throw IllegalArgumentException("Invalid path to the bam file to parse: [$bamFilePath]")
        }
    }

    private val samReader = samReaderFactory
        .validationStringency(ValidationStringency.STRICT)
        .open(bamFilePath)

    override fun parseRegion(feature: Feature): List<SAMRecord> {
        if (samReader.fileHeader.getSequence(feature.chromosome) == null) {
            return emptyList()
        }

        return with(feature) {
            samReader.query(chromosome, start, end, false).toList()
        }
    }

    override fun close() {
        samReader.close()
    }

    private companion object {
        const val BAM_FILE_EXTENSION = "bam"
    }
}