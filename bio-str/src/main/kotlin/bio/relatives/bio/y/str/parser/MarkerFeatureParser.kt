package bio.relatives.bio.y.str.parser

import bio.relatives.bio.y.str.model.MarkerFeature
import bio.relatives.common.model.Feature
import bio.relatives.common.parser.AbstractFeatureParser
import bio.relatives.common.utils.ALLOWED_NUCLEOTIDES
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 02.02.2021
 */
@Component
class MarkerFeatureParser : AbstractFeatureParser() {
    override fun getFeaturesFromRows(rows: Array<String>, featureFilePath: Path): List<Feature> {
        val res = mutableListOf<MarkerFeature>()

        require(rows.size == 5) {
            "Error occurred during reading from the file [$featureFilePath]: " +
                    "incorrect number of rows in the table. Expected 5 (chrom, start, end, gene name, repeatMotif), got ${rows.size}"
        }

        require(rows[4].all { nucl -> ALLOWED_NUCLEOTIDES.contains(nucl) }) {
            "Error occurred during reading from the file [$featureFilePath]: " +
                    "incorrect repeatMotif ${rows[4]}"
        }

        val chrom = rows[0]
        val gene = rows[3]
        val start = rows[1].toInt()
        val end = rows[2].toInt()
        val repeatMotif = Pattern.compile(rows[4])

        val exonLen = end - start
        if (exonLen <= MAX_FEATURE_SIZE) {
            res.add(MarkerFeature(chrom, gene, start, end, repeatMotif))
        } else {
            var tempStart = start
            for (tempEnd in start..end) {
                if (tempEnd - tempStart > MAX_FEATURE_SIZE) {
                    res.add(MarkerFeature(chrom, gene, tempStart, tempEnd, repeatMotif))
                    tempStart = tempEnd + 1
                }
            }
            if (end - tempStart > 1) {
                res.add(MarkerFeature(chrom, gene, tempStart, end, repeatMotif))
            }
        }

        return res
    }
}
