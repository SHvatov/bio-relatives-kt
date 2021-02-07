package bio.relatives.bio.distance.parser

import bio.relatives.bio.distance.model.BedFeature
import bio.relatives.common.model.Feature
import bio.relatives.common.parser.AbstractFeatureParser
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 02.02.2021
 */
@Component
class BedFeatureParser : AbstractFeatureParser() {
    override fun getFeaturesFromRows(rows: Array<String>, featureFilePath: Path): List<Feature> {
        val res = mutableListOf<BedFeature>()

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
            res.add(BedFeature(chrom, gene, start, end))
        } else {
            var tempStart = start
            for (tempEnd in start..end) {
                if (tempEnd - tempStart > MAX_FEATURE_SIZE) {
                    res.add(BedFeature(chrom, gene, tempStart, tempEnd))
                    tempStart = tempEnd + 1
                }
            }
            if (end - tempStart > 1) {
                res.add(BedFeature(chrom, gene, tempStart, end))
            }
        }

        return res
    }
}
