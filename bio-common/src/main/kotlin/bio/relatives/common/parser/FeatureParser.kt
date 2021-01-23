package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import java.nio.file.Path

/**
 * @author shvatov
 */
interface FeatureParser {
    fun parseFeatures(featureFilePath: Path): List<Feature>
}