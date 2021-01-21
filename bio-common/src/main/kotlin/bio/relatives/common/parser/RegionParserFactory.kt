package bio.relatives.common.parser

import java.nio.file.Path

/**
 * @author shvatov
 */
interface RegionParserFactory {
    fun create(bamFilePath: Path): FeatureParser
}