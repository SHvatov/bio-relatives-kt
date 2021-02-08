package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import java.nio.file.Path

/**
 * @author shvatov
 */
interface FeatureParser {
    /**
     * Parses feature file along the path [featureFilePath] into list of [Feature]
     */
    fun parseFeatures(featureFilePath: Path): List<Feature>
}