package bio.relatives.bio.y.str.comparator

import bio.relatives.bio.y.str.model.MarkerFeature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware
import org.junit.Test
import java.util.regex.Pattern
import kotlin.test.assertEquals

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioYStrGenomeComparatorAlgorithmTest {

    companion object {
        private val LEFT_ROLE = RoleAware.Role.FATHER
        private const val LEFT_SEQUENCE = "agcttagctcagctcg*agctta"

        private val RIGHT_ROLE = RoleAware.Role.SON
        private const val RIGHT_SEQUENCE = "agctagcttaagctcg*ttata"

        private val LEFT_BASE_QUALITIES = arrayOf(
                25.0, 50.0, 30.0, 10.0, 40.0,
                50.0, 90.0, 70.0, 1.0, 2.0,
                4.0, 12.0, 13.0, 14.0,
                15.0, 16.0, 17.0, 18.0, 19.0,
                20.0, 21.0, 22.0
        )

        private val RIGHT_BASE_QUALITIES = arrayOf(
                1.0, 2.0, 4.0, 12.0, 13.0,
                14.0, 15.0, 16.0, 17.0, 18.0,
                19.0, 20.0, 21.0, 22.0, 30.0, 40.0, 10.0,
                20.0, 35.0, 41.0, 52.0, 53.0, 10.0, 11.0, 12.0
        )

        private const val CHR = "chr1"
        private const val GENE = "gene1"
        private const val START = 0
        private const val END = 21
        private val REPEAT_MOTIF = Pattern.compile("agct")

        private val LEFT = Region(LEFT_ROLE, LEFT_SEQUENCE, LEFT_BASE_QUALITIES, GENE, CHR, START, END)

        private val RIGHT = Region(RIGHT_ROLE, RIGHT_SEQUENCE, RIGHT_BASE_QUALITIES, GENE, CHR, START, END)

        private val BIO_Y_STR_GENOME_COMPARATOR_ALGORITHM = BioYStrGenomeComparatorAlgorithm()

        private val Y_STR_FEATURE = MarkerFeature(GENE, CHR, START, END, REPEAT_MOTIF)

        private const val ERROR_RATE = 77.3
        private const val SIMILARITY = 75.0
    }

    @Test
    fun compare() {
        val res = BIO_Y_STR_GENOME_COMPARATOR_ALGORITHM.compare(LEFT, RIGHT, Y_STR_FEATURE)
        assertEquals(ERROR_RATE, res.errorRate)
        assertEquals(SIMILARITY, res.similarityPercentage)
    }
}