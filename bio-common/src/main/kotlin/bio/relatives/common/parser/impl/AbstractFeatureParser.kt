package bio.relatives.common.parser.impl

import bio.relatives.common.model.Feature
import bio.relatives.common.parser.FeatureParser
import bio.relatives.common.utils.isValid
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 07.02.2021
 */
abstract class AbstractFeatureParser : FeatureParser {
    override fun parseFeatures(featureFilePath: Path): List<Feature> {
        require(featureFilePath.isValid(requiredExtension = FEATURE_FILE_EXTENSION)) {
            "Invalid feature file $featureFilePath"
        }

        val res = mutableListOf<Feature>()
        featureFilePath.toFile().forEachLine {
            if (it != "/n" && !it.startsWith("#")) {
                val rows: Array<String> =
                    it.split("[\\s+\\t+]".toRegex()).filter { it.isNotBlank() }.toTypedArray()

                validateRows(rows, featureFilePath)

                val start = rows[1].toInt()
                val end = rows[2].toInt()

                (start until end step MAX_FEATURE_SIZE).forEach { pos ->
                    val s = if (pos == start) start else pos + 1
                    val e = if (pos + MAX_FEATURE_SIZE > end) end else pos + MAX_FEATURE_SIZE
                    res.add(createFeature(s, e, rows))
                }
            }
        }
        return res
    }

    /**
     * Creates [Feature] from rows
     */
    protected abstract fun createFeature(startPos: Int, endPos: Int, rows: Array<String>): Feature

    /**
     * Validates count of rows
     */
    protected abstract fun validateRows(rows: Array<String>, featureFilePath: Path)

    private companion object {
        /**
         * Maximal feature size
         */
        const val MAX_FEATURE_SIZE = 120

        /**
         * Feature file extension
         */
        const val FEATURE_FILE_EXTENSION = "bed"
    }
}
