package bio.relatives.assemble.de.bruijn.sorter

import htsjdk.samtools.SAMRecord

/**
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
class SAMRecordsSorterImpl : SAMRecordsSorter {
    override fun sort(samRecords: List<SAMRecord>): List<SAMRecord> = samRecords
            .toMutableList()
            .sortedWith { s1, s2 ->
                when {
                    (s1.start > s2.start) || (
                            s1.start.compareTo(s2.start) == 0 && s1.end > s2.end
                            ) -> 1
                    (s1.start.compareTo(s2.start) == 0
                            && s1.end.compareTo(s2.end) == 0) -> 0
                    else -> -1
                }
            }
}