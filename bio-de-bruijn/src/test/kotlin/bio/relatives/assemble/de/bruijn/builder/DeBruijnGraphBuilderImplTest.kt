package bio.relatives.assemble.de.bruijn.builder

import bio.relatives.assemble.de.bruijn.analyzer.DeBruijnGraphAnalyzerImpl
import bio.relatives.assemble.de.bruijn.sorter.SAMRecordsSorterImpl
import bio.relatives.assemble.de.bruijn.utils.*
import htsjdk.samtools.SAMRecord
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * @author Created by Vladislav Marchenko on 29.01.2021
 */
class DeBruijnGraphBuilderImplTest {

    private val TEST_SAMRECORD_1: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_2: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_3: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_4: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_5: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_6: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_KMER_SIZE = 3

    private val TEST_SAMRECORDS: List<SAMRecord> = listOf(
            TEST_SAMRECORD_1,
            TEST_SAMRECORD_2,
            TEST_SAMRECORD_3,
            TEST_SAMRECORD_4,
            TEST_SAMRECORD_5,
            TEST_SAMRECORD_6
    )

    @Before
    fun setUp() {
        Mockito.`when`(TEST_SAMRECORD_1.readString).thenReturn(TEST_READ_STRING_1)
        Mockito.`when`(TEST_SAMRECORD_2.readString).thenReturn(TEST_READ_STRING_2)
        Mockito.`when`(TEST_SAMRECORD_3.readString).thenReturn(TEST_READ_STRING_3)
        Mockito.`when`(TEST_SAMRECORD_4.readString).thenReturn(TEST_READ_STRING_4)
        Mockito.`when`(TEST_SAMRECORD_5.readString).thenReturn(TEST_READ_STRING_5)
        Mockito.`when`(TEST_SAMRECORD_6.readString).thenReturn(TEST_READ_STRING_6)

        Mockito.`when`(TEST_SAMRECORD_1.baseQualities).thenReturn(TEST_BASE_QUALITIES_1)
        Mockito.`when`(TEST_SAMRECORD_2.baseQualities).thenReturn(TEST_BASE_QUALITIES_2)
        Mockito.`when`(TEST_SAMRECORD_3.baseQualities).thenReturn(TEST_BASE_QUALITIES_3)
        Mockito.`when`(TEST_SAMRECORD_4.baseQualities).thenReturn(TEST_BASE_QUALITIES_4)
        Mockito.`when`(TEST_SAMRECORD_5.baseQualities).thenReturn(TEST_BASE_QUALITIES_5)
        Mockito.`when`(TEST_SAMRECORD_6.baseQualities).thenReturn(TEST_BASE_QUALITIES_6)
    }

    @Test
    fun buildGraph() {
        val builder = DeBruijnGraphBuilderImpl()
        val sorter = SAMRecordsSorterImpl()
        val analyzer = DeBruijnGraphAnalyzerImpl()
        println(analyzer.analyze(builder.buildGraph(sorter.sort(TEST_SAMRECORDS), TEST_KMER_SIZE)))
    }
}