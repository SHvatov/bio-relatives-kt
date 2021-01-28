package bio.relatives.assemble.de.bruijn.sorter

import htsjdk.samtools.SAMRecord

/**
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
interface SAMRecordsSorter {
    /**
     * Sorts input list of SAMRecords by start position of reads which contains in these SAMRecords
     */
    fun sort(samRecords: List<SAMRecord>): List<SAMRecord>
}