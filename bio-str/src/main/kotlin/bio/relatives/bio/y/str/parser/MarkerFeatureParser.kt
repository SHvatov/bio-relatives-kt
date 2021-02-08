package bio.relatives.bio.y.str.parser

import bio.relatives.bio.y.str.model.MarkerFeature
import bio.relatives.common.model.Feature
import bio.relatives.common.parser.impl.AbstractFeatureParser
import bio.relatives.common.utils.ALLOWED_NUCLEOTIDES
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 02.02.2021
 */
@Component
class MarkerFeatureParser : AbstractFeatureParser() {
    override fun provider(rows: Array<String>): Feature {
        val chr = rows[0]
        val gene = rows[3]
        val start = rows[1].toInt()
        val end = rows[2].toInt()
        val repeatMotif = Pattern.compile(rows[4])

        return MarkerFeature(gene, chr, start, end, repeatMotif)
    }

    override fun validateRows(rows: Array<String>, featureFilePath: Path) {
        require(rows.size == 5 && rows[4].all { ALLOWED_NUCLEOTIDES.contains(it) }) {
            "Error occurred during reading from the file [$featureFilePath]: " +
                    "incorrect number of rows in the table. Expected 5 (chrom, start, end, gene name, repeatMotif), got ${rows.size}"
        }
    }

}
