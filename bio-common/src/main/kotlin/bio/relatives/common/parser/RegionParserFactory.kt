package bio.relatives.common.parser

import java.nio.file.Path

/**
 * @author shvatov
 */
interface RegionParserFactory {
    /**
     * Creates an instance of the [RegionParser], which will be then used to parse
     * BAM file located by the provided [bamFilePath].
     */
    fun create(bamFilePath: Path): RegionParser
}