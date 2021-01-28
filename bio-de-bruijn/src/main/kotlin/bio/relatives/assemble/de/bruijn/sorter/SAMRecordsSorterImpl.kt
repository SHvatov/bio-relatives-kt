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
                    (s1.alignmentStart > s2.alignmentStart) || (
                            s1.alignmentStart.compareTo(s2.alignmentStart) == 0 && s1.alignmentEnd > s2.alignmentEnd
                            ) -> 1
                    (s1.alignmentStart.compareTo(s2.alignmentStart) == 0
                            && s1.alignmentEnd.compareTo(s2.alignmentEnd) == 0) -> 0
                    else -> -1
                }
            }
}