package bio.relatives.bio.distance.parser

import bio.relatives.bio.distance.model.BEDFeature
import bio.relatives.common.model.Feature
import bio.relatives.common.parser.FeatureParser
import bio.relatives.common.parser.FeatureParser.Companion.FEATURE_FILE_EXTENSION
import bio.relatives.common.parser.FeatureParser.Companion.MAX_FEATURE_SIZE
import bio.relatives.common.utils.isValid
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 02.02.2021
 */
@Component
class BEDFeatureParser : FeatureParser {
    override fun parseFeatures(featureFilePath: Path): List<Feature> {
        featureFilePath.isValid(requiredExtension = FEATURE_FILE_EXTENSION)

        val res = mutableListOf<BEDFeature>()

        featureFilePath.toFile().forEachLine {
            run {
                if (it != "/n" && !it.startsWith("#")) {
                    val rows: Array<String> = it.split("[\\s+\\t+]".toRegex()).filter { it.isNotBlank() }.toTypedArray()

                    require(rows.size == 4) {
                        "Error occurred during reading from the file [$featureFilePath]: " +
                                "incorrect number of rows in the table. Expected 4 (chrom, start, end, gene name), got ${rows.size}"
                    }

                    val chrom = rows[0]
                    val gene = rows[3]
                    val start = rows[1].toInt()
                    val end = rows[2].toInt()

                    val exonLen = end - start
                    if (exonLen <= MAX_FEATURE_SIZE) {
                        res.add(BEDFeature(chrom, gene, start, end))
                    } else {
                        var tempStart = start
                        for (tempEnd in start..end) {
                            if (tempEnd - tempStart > MAX_FEATURE_SIZE) {
                                res.add(BEDFeature(chrom, gene, tempStart, tempEnd))
                                tempStart = tempEnd + 1
                            }
                        }
                        if (end - tempStart > 1) {
                            res.add(BEDFeature(chrom, gene, tempStart, end))
                        }
                    }
                }
            }
        }

        return res
    }
}