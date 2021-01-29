package bio.relatives.assemble.de.bruijn.sorter

import bio.relatives.assemble.de.bruijn.utils.*
import htsjdk.samtools.SAMRecord
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * @author Created by Vladislav Marchenko on 29.01.2021
 */
class SAMRecordsSorterImplTest {

    private val TEST_SAMRECORD_1: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_2: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_3: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_4: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_5: SAMRecord = Mockito.mock(SAMRecord::class.java)

    private val TEST_SAMRECORD_6: SAMRecord = Mockito.mock(SAMRecord::class.java)


    private val TEST_SAMRECORDS: List<SAMRecord> = listOf(
            TEST_SAMRECORD_2,
            TEST_SAMRECORD_1,
            TEST_SAMRECORD_4,
            TEST_SAMRECORD_5,
            TEST_SAMRECORD_6,
            TEST_SAMRECORD_3
    )

    private val CHECK_START_POS_LIST = listOf(
            0, 0, 2, 3, 3, 5
    )

    private val CHECK_END_POS_LIST = listOf(
            4, 4, 6, 7, 8, 9
    )

    @Before
    fun setUp() {
        Mockito.`when`(TEST_SAMRECORD_1.start).thenReturn(TEST_READ_START_1)
        Mockito.`when`(TEST_SAMRECORD_2.start).thenReturn(TEST_READ_START_2)
        Mockito.`when`(TEST_SAMRECORD_3.start).thenReturn(TEST_READ_START_3)
        Mockito.`when`(TEST_SAMRECORD_4.start).thenReturn(TEST_READ_START_4)
        Mockito.`when`(TEST_SAMRECORD_5.start).thenReturn(TEST_READ_START_5)
        Mockito.`when`(TEST_SAMRECORD_6.start).thenReturn(TEST_READ_START_6)

        Mockito.`when`(TEST_SAMRECORD_1.end).thenReturn(TEST_READ_END_1)
        Mockito.`when`(TEST_SAMRECORD_2.end).thenReturn(TEST_READ_END_2)
        Mockito.`when`(TEST_SAMRECORD_3.end).thenReturn(TEST_READ_END_3)
        Mockito.`when`(TEST_SAMRECORD_4.end).thenReturn(TEST_READ_END_4)
        Mockito.`when`(TEST_SAMRECORD_5.end).thenReturn(TEST_READ_END_5)
        Mockito.`when`(TEST_SAMRECORD_6.end).thenReturn(TEST_READ_END_6)
    }

    @Test
    fun sort() {
        val sorter = SAMRecordsSorterImpl()
        val sortedList = sorter.sort(TEST_SAMRECORDS)
        val startPosList = ArrayList<Int>()
        val endPosList = ArrayList<Int>()
        sortedList.forEach {
            run {
                startPosList.add(it.start)
                endPosList.add(it.end)
            }
        }
        assertEquals(CHECK_START_POS_LIST, startPosList)
        assertEquals(CHECK_END_POS_LIST, endPosList)
    }
}