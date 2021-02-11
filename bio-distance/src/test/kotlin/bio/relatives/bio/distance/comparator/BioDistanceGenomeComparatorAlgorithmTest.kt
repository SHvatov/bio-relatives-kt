package bio.relatives.bio.distance.comparator

import bio.relatives.bio.distance.model.BedFeature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioDistanceGenomeComparatorAlgorithmTest {

    companion object {
        private val LEFT_ROLE = RoleAware.Role.FATHER
        private const val LEFT_SEQUENCE = "a*gctagctcgtaacg*ttata"

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
                20.0, 35.0, 41.0, 52.0, 53.0, 12.0, 11.0, 12.0
        )

        private val RIGHT_ROLE = RoleAware.Role.SON
        private const val RIGHT_SEQUENCE = "aagctagatagtgacg*ttata"

        private const val CHR = "chr1"
        private const val GENE = "gene1"
        private const val START = 0
        private const val END = 21

        private val LEFT = Region(LEFT_ROLE, LEFT_SEQUENCE, LEFT_BASE_QUALITIES, GENE, CHR, START, END)

        private val RIGHT = Region(RIGHT_ROLE, RIGHT_SEQUENCE, RIGHT_BASE_QUALITIES, GENE, CHR, START, END)

        private val BIO_DISTANCE_GENOME_COMPARATOR_ALGORITHM = LevensteinDistanceAlgorithm()

        private val BED_FEATURE = BedFeature(CHR, GENE, START, END)

        private const val ERROR_RATE = 77.25531914893617
        private const val SIMILARITY = 85.0
    }


    @Test
    fun compare() {
        val res = BIO_DISTANCE_GENOME_COMPARATOR_ALGORITHM.compare(BED_FEATURE, LEFT, RIGHT)
        assertEquals(ERROR_RATE, res.errorRate)
        assertEquals(SIMILARITY, res.similarityPercentage)
    }
}