package bio.relatives.bio.distance.comparator

import bio.relatives.bio.distance.model.BioDistanceAlgorithmResult
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioDistanceGenomeComparatorAlgorithmTest {

    companion object {
        private val LEFT_ROLE = RoleAware.Role.FATHER
        private const val LEFT_SEQUENCE = "a*gctagctcgtaacg*ttata"

        private val RIGHT_ROLE = RoleAware.Role.SON
        private const val RIGHT_SEQUENCE = "aagctagatagtgacg*ttata"

        private const val CHR = "chr1"
        private const val GENE = "gene1"
        private const val START = 0
        private const val END = 21
        private val REPEAT_MOTIF = Pattern.compile("agct")

        private val LEFT = Region(LEFT_ROLE, LEFT_SEQUENCE, CHR, GENE, START, END, REPEAT_MOTIF)

        private val RIGHT = Region(RIGHT_ROLE, RIGHT_SEQUENCE, CHR, GENE, START, END, REPEAT_MOTIF)

        private val BIO_DISTANCE_GENOME_COMPARATOR_ALGORITHM = BioDistanceGenomeComparatorAlgorithm()

        private const val DIFFERENCE = 3

        private const val LENGTH = 20
    }


    @Test
    fun compare() {
        val res = BIO_DISTANCE_GENOME_COMPARATOR_ALGORITHM.compare(LEFT, RIGHT)
        if (res is BioDistanceAlgorithmResult) {
            assertEquals(DIFFERENCE, res.result.first)
            assertEquals(LENGTH, res.result.second)
        }
    }
}