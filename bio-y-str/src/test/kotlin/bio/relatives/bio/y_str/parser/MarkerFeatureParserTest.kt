package bio.relatives.bio.y_str.parser

import bio.relatives.bio.y_str.model.MarkerFeature
import bio.relatives.common.model.Feature
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 03.02.2021
 */
class MarkerFeatureParserTest {
    @Test
    fun parseFeaturesFromCorrectBED() {
        val res = MFP.parseFeatures(PATH_TO_CORRECT_BED)

        assertEquals(3, res.size)
        assertRecordsEqual(FIRST_FEATURE_0, res[0])
        assertRecordsEqual(FIRST_FEATURE_1, res[1])
        assertRecordsEqual(SECOND_FEATURE, res[2])
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseFeaturesFromInCorrectBED() {
        MFP.parseFeatures(PATH_TO_INCORRECT_BED)
    }

    private fun assertRecordsEqual(expected: MarkerFeature, actual: Feature) {
        actual as MarkerFeature
        assertEquals(expected.start, actual.start)
        assertEquals(expected.end, actual.end)
        assertEquals(expected.chromosome, actual.chromosome)
        assertEquals(expected.gene, actual.gene)
        assertEquals(expected.repeatMotif.pattern(), actual.repeatMotif.pattern())
    }

    private companion object {
        val PATH_TO_CORRECT_BED: Path = Paths.get("src/test/resources/correct.bed")
        val PATH_TO_INCORRECT_BED: Path = Paths.get("src/test/resources/incorrect.bed")
        val FIRST_FEATURE_0 = MarkerFeature("SCYL3", "chr1", 15000, 15120, Pattern.compile("agct"))
        val FIRST_FEATURE_1 = MarkerFeature("SCYL3", "chr1", 15121, 15239, Pattern.compile("agct"))
        val SECOND_FEATURE = MarkerFeature("SCYL3", "chr1", 20000, 20119, Pattern.compile("agc"))
        val MFP = MarkerFeatureParser()
    }
}