package bio.relatives.bio.y.str.comparator

import bio.relatives.bio.y.str.model.BioYStrAlgorithmResult
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioYStrGenomeComparatorAlgorithmTest {

    companion object {
        private val LEFT_ROLE = RoleAware.Role.FATHER
        private const val LEFT_SEQUENCE = "agcttagctcagctcg*agctta"

        private val RIGHT_ROLE = RoleAware.Role.SON
        private const val RIGHT_SEQUENCE = "agctagcttaagctcg*ttata"

        private const val CHR = "chr1"
        private const val GENE = "gene1"
        private const val START = 0
        private const val END = 21
        private val REPEAT_MOTIF = Pattern.compile("agct")

        private val LEFT = Region(LEFT_ROLE, LEFT_SEQUENCE, CHR, GENE, START, END, REPEAT_MOTIF)

        private val RIGHT = Region(RIGHT_ROLE, RIGHT_SEQUENCE, CHR, GENE, START, END, REPEAT_MOTIF)

        private val BIO_Y_STR_GENOME_COMPARATOR_ALGORITHM = BioYStrGenomeComparatorAlgorithm()

        private const val FIRST_REPEATS = 4

        private const val SECOND_REPEATS = 3
    }

    @Test
    fun compare() {
        val res = BIO_Y_STR_GENOME_COMPARATOR_ALGORITHM.compare(LEFT, RIGHT)
        if (res is BioYStrAlgorithmResult) {
            assertEquals(FIRST_REPEATS, res.result.first)
            assertEquals(SECOND_REPEATS, res.result.second)
        }
    }
}