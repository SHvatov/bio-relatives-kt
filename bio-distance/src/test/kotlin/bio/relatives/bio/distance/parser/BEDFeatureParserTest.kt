package bio.relatives.bio.distance.parser

import bio.relatives.bio.distance.model.BedFeature
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Created by Vladislav Marchenko on 03.02.2021
 */
internal class BEDFeatureParserTest {
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
        BFP.parseFeatures(PATH_TO_INCORRECT_BED)
    }

    private companion object {
        val PATH_TO_CORRECT_BED: Path = Paths.get("src/test/resources/correct.bed")
        val PATH_TO_INCORRECT_BED: Path = Paths.get("src/test/resources/incorrect.bed")
        val FIRST_FEATURE = BedFeature("chr1", "SCYL3", 15000, 15119)
        val SECOND_FEATURE = BedFeature("chr1", "C1orf112", 20000, 20119)
        val BFP = BedFeatureParser()
    }
}