package bio.relatives.assemble.de.bruijn.assembler.naive.assembler

import bio.relatives.assemble.naive.assembler.NaiveRegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.RoleAware
import htsjdk.samtools.SAMRecord
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
internal class NaiveRegionAssemblerTest {
    @Before
    fun setUp() {
        Mockito.`when`(TEST_FEATURE.start).thenReturn(TEST_GENOME_START)
        Mockito.`when`(TEST_FEATURE.end).thenReturn(TEST_GENOME_END)
        Mockito.`when`(TEST_FEATURE.gene).thenReturn(TEST_GENE)
        Mockito.`when`(TEST_FEATURE.chromosome).thenReturn(TEST_CHR)

        Mockito.`when`(TEST_SAMRECORDS[0].readLength).thenReturn(TEST_READ_LENGTH_1)
        Mockito.`when`(TEST_SAMRECORDS[1].readLength).thenReturn(TEST_READ_LENGTH_2)
        Mockito.`when`(TEST_SAMRECORDS[2].readLength).thenReturn(TEST_READ_LENGTH_3)

        Mockito.`when`(TEST_SAMRECORDS[0].readString).thenReturn(TEST_READ_STRING_1)
        Mockito.`when`(TEST_SAMRECORDS[1].readString).thenReturn(TEST_READ_STRING_2)
        Mockito.`when`(TEST_SAMRECORDS[2].readString).thenReturn(TEST_READ_STRING_3)

        Mockito.`when`(TEST_SAMRECORDS[0].baseQualities).thenReturn(TEST_BASE_QUALITIES_1)
        Mockito.`when`(TEST_SAMRECORDS[1].baseQualities).thenReturn(TEST_BASE_QUALITIES_2)
        Mockito.`when`(TEST_SAMRECORDS[2].baseQualities).thenReturn(TEST_BASE_QUALITIES_3)

        Mockito.`when`(TEST_SAMRECORDS[0].start).thenReturn(TEST_READ_START_1)
        Mockito.`when`(TEST_SAMRECORDS[1].start).thenReturn(TEST_READ_START_2)
        Mockito.`when`(TEST_SAMRECORDS[2].start).thenReturn(TEST_READ_START_3)
    }

    @Test
    fun assemble() {
        val assembler = NaiveRegionAssembler()
        assertEquals(
            CHECK_SEQUENCE,
            assembler.assemble(RoleAware.Role.FATHER, TEST_FEATURE, TEST_SAMRECORDS).sequence
        )
    }

    private companion object {
        val TEST_SAMRECORD_1: SAMRecord = Mockito.mock(SAMRecord::class.java)

        val TEST_SAMRECORD_2: SAMRecord = Mockito.mock(SAMRecord::class.java)

        val TEST_SAMRECORD_3: SAMRecord = Mockito.mock(SAMRecord::class.java)

        val TEST_SAMRECORDS: List<SAMRecord> = listOf(
            TEST_SAMRECORD_1,
            TEST_SAMRECORD_2,
            TEST_SAMRECORD_3
        )

        val TEST_FEATURE: Feature = Mockito.mock(Feature::class.java)

        const val TEST_GENOME_START = 0

        const val TEST_GENOME_END = 11

        const val TEST_CHR = "chr1"

        const val TEST_GENE = "gene1"

        const val TEST_READ_LENGTH_1 = 8

        const val TEST_READ_LENGTH_2 = 11

        const val TEST_READ_LENGTH_3 = 11

        const val TEST_READ_STRING_1 = "ahgcetat"

        const val TEST_READ_STRING_2 = "fgctagctagc"

        const val TEST_READ_STRING_3 = "factggcatac"

        const val TEST_READ_START_1 = 0
        const val TEST_READ_START_2 = 1
        const val TEST_READ_START_3 = 1

        val TEST_BASE_QUALITIES_1 = arrayOf<Byte>(
            25, 50, 30, 10, 40, 50, 90, 70
        ).toByteArray()

        val TEST_BASE_QUALITIES_2 = arrayOf<Byte>(
            30, 40, 10, 20, 35, 41, 52, 53, 10, 11, 12
        ).toByteArray()

        val TEST_BASE_QUALITIES_3 = arrayOf<Byte>(
            10, 25, 30, 40, 41, 42, 43, 44, 43, 42, 41
        ).toByteArray()

        const val CHECK_SEQUENCE = "a*gcttgcttac"
    }
}