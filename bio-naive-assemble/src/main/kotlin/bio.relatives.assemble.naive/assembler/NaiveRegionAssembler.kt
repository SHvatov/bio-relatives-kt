package bio.relatives.assemble.naive.assembler

import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware
import bio.relatives.common.utils.ALLOWED_NUCLEOTIDES
import bio.relatives.common.utils.UNKNOWN_NUCLEOTIDE
import bio.relatives.common.utils.getMedianQuality
import htsjdk.samtools.SAMRecord
import java.util.*

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
class NaiveRegionAssembler : RegionAssembler {

    override fun assemble(feature: Feature, records: List<SAMRecord>): Region {

        val nucleotideSequence = StringBuilder()

        for (i in feature.start..feature.end) {

            val currentNucleotides: Map<Char, List<Byte>> = getNucleotideDistribution(records, i)

            var bestNucleotide: Char = UNKNOWN_NUCLEOTIDE

            var bestQuality: Byte = 0

            var bestCount = 0

            val set = currentNucleotides.entries
            for ((key, value) in set) {

                if (value.size > bestCount) {
                    bestCount = value.size
                    bestNucleotide = key
                    bestQuality = getMedianQuality(value)
                }

                if (value.size == bestCount) {

                    if (getMedianQuality(value) > bestQuality) {
                        bestCount = value.size
                        bestNucleotide = key
                        bestQuality = getMedianQuality(value)
                    }
                }
            }
            nucleotideSequence.append(bestNucleotide)
        }
        TODO("change role to the natural role")
        return Region(RoleAware.Role.FATHER, nucleotideSequence.toString(), feature.chromosome, feature.gene, feature.start, feature.end)
    }

    private fun getNucleotideDistribution(records: List<SAMRecord>, position: Int): Map<Char, List<Byte>> {

        val dist: MutableMap<Char, MutableList<Byte>> = HashMap()
        for (nucleotide in ALLOWED_NUCLEOTIDES.toCharArray()) {
            dist[nucleotide] = ArrayList()
        }

        for (rec in records) {
            val pos = position - rec.start
            var currentNucleotide = ' '
            var currentQuality: Byte = 0
            if (pos < rec.readLength && pos >= 0) {
                currentNucleotide = Character.toLowerCase(rec.readString[pos])
                currentQuality = rec.baseQualities[pos]
            }
            if (ALLOWED_NUCLEOTIDES.contains(currentNucleotide.toString())) {
                dist[currentNucleotide]!!.add(currentQuality)
            }
        }
        return dist
    }
}