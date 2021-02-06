package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import bio.relatives.common.utils.isValid
import java.nio.file.Path

/**
 * @author shvatov
 */
interface FeatureParser {
    fun parseFeatures(featureFilePath: Path): List<Feature>

    fun isValidFile(featureFilePath: Path) {
        require(featureFilePath.isValid(requiredExtension = FeatureParser.FEATURE_FILE_EXTENSION)) {
            "Invalid path to the bed file to parse: [$featureFilePath]"
        }
    }

    companion object {
        const val MAX_FEATURE_SIZE = 120
        const val FEATURE_FILE_EXTENSION = "bed"
    }
}