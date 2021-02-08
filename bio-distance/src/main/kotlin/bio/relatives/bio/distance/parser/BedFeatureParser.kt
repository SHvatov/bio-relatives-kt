package bio.relatives.bio.distance.parser

import bio.relatives.bio.distance.model.BedFeature
import bio.relatives.common.model.Feature
import bio.relatives.common.parser.impl.AbstractFeatureParser
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 02.02.2021
 */
@Component
class BedFeatureParser : AbstractFeatureParser() {
    override fun provider(rows: Array<String>): Feature {
        val chr = rows[0]
        val start = rows[1].toInt()
        val end = rows[2].toInt()
        val gene = rows[3]
        return BedFeature(chr, gene, start, end)
    }

    override fun validateRows(rows: Array<String>, featureFilePath: Path) {
        require(rows.size == 4) {
            "Error occurred during reading from the file [$featureFilePath]: " +
                    "incorrect number of rows in the table. Expected 4 (chrom, start, end, gene name), got ${rows.size}"
        }
    }

}
