package bio.relatives.bio.y.str.parser

import bio.relatives.bio.y.str.model.MarkerFeature
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 03.02.2021
 */
class MarkerFeatureParserTest {
    companion object {
        private val PATH_TO_CORRECT_BED: Path = Paths.get("src/test/resources/correct.bed")
        private val PATH_TO_INCORRECT_BED: Path = Paths.get("src/test/resources/incorrect.bed")
        private val FIRST_FEATURE = MarkerFeature("chr1", "SCYL3", 15000, 15119, Pattern.compile("agct"))
        private val SECOND_FEATURE = MarkerFeature("chr1", "SCYL3", 20000, 20119, Pattern.compile("agc"))
        private val MFP = MarkerFeatureParser()
    }

    @Test
    fun parseFeaturesFromCorrectBED() {
        val res = MFP.parseFeatures(PATH_TO_CORRECT_BED)

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
        val res = MFP.parseFeatures(PATH_TO_INCORRECT_BED)
    }
}