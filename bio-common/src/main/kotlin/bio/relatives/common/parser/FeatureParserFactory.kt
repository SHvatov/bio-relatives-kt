package bio.relatives.common.parser

import java.nio.file.Path

/**
 * @author shvatov
 */
interface FeatureParserFactory {
    fun create(featureFilePath: Path): FeatureParser
}