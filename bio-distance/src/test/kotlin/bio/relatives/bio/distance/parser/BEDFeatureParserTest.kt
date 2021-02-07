package bio.relatives.bio.distance.parser

import bio.relatives.bio.distance.model.BedFeature
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals


/**
 * @author Created by Vladislav Marchenko on 03.02.2021
 */
class BEDFeatureParserTest {

    companion object {
        private val PATH_TO_CORRECT_BED: Path = Paths.get("src/test/resources/correct.bed")
        private val PATH_TO_INCORRECT_BED: Path = Paths.get("src/test/resources/incorrect.bed")
        private val FIRST_FEATURE = BedFeature("chr1", "SCYL3", 15000, 15119)
        private val SECOND_FEATURE = BedFeature("chr1", "C1orf112", 20000, 20119)
        private val BFP = BedFeatureParser()
    }

    @Test
    fun parseFeaturesFromCorrectBED() {
        val res = BFP.parseFeatures(PATH_TO_CORRECT_BED)
        assertEquals(FIRST_FEATURE.start, res[0].start)
        assertEquals(FIRST_FEATURE.end, res[0].end)
        assertEquals(FIRST_FEATURE.chromosome, res[0].chromosome)
        assertEquals(FIRST_FEATURE.gene, res[0].gene)

        assertEquals(SECOND_FEATURE.start, res[1].start)
        assertEquals(SECOND_FEATURE.end, res[1].end)
        assertEquals(SECOND_FEATURE.chromosome, res[1].chromosome)
        assertEquals(SECOND_FEATURE.gene, res[1].gene)
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseFeaturesFromInCorrectBED() {
        val res = BFP.parseFeatures(PATH_TO_INCORRECT_BED)
    }
}