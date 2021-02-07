package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import bio.relatives.common.utils.isValid
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 07.02.2021
 */
abstract class AbstractFeatureParser {
    /**
     * Parses feature file along the path [featureFilePath] into list of [Feature]
     */
    fun parseFeatures(featureFilePath: Path): List<Feature> {
        require(featureFilePath.isValid(requiredExtension = FEATURE_FILE_EXTENSION)) {
            "Invalid feature file $featureFilePath"
        }

        val res = mutableListOf<Feature>()

        featureFilePath.toFile().forEachLine {
            if (it != "/n" && !it.startsWith("#")) {
                val rows: Array<String> = it.split("[\\s+\\t+]".toRegex()).filter { it.isNotBlank() }.toTypedArray()

                res.addAll(getFeaturesFromRows(rows, featureFilePath))
            }
        }

        return res
    }

    /**
     * Creates list of [Feature] from rows
     */
    abstract fun getFeaturesFromRows(rows: Array<String>, featureFilePath: Path): List<Feature>

    companion object {
        /**
         * Maximal feature size
         */
        const val MAX_FEATURE_SIZE = 120

        /**
         * Feature file extension
         */
        private const val FEATURE_FILE_EXTENSION = "bed"
    }
}
