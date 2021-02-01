package bio.relatives.assemble.naive.assembler

import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.utils.ALLOWED_NUCLEOTIDES
import bio.relatives.common.utils.UNKNOWN_NUCLEOTIDE
import bio.relatives.common.utils.calculateMedianVale
import htsjdk.samtools.SAMRecord
import java.util.ArrayList
import java.util.HashMap

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
class NaiveRegionAssembler : RegionAssembler {
    /**
     * Atomic [nucleotide] chosen based on its [quality].
     */
    private data class Nucleotide(val nucleotide: Char, val quality: Double)

    override fun assemble(role: Role, feature: Feature, records: List<SAMRecord>): Region {
        val nucleotideSequence = mutableListOf<Nucleotide>()

        for (position in feature.start..feature.end) {
            val currentNucleotides = getNucleotideDistribution(records, position)

            var bestNucleotide = UNKNOWN_NUCLEOTIDE
            var bestQuality = 0.0
            var bestCount = 0

            for ((key, value) in currentNucleotides) {
                if (value.size > bestCount) {
                    bestCount = value.size
                    bestNucleotide = key
                    bestQuality = calculateMedianVale(value)
                }

                if (value.size == bestCount) {
                    if (calculateMedianVale(value) > bestQuality) {
                        bestCount = value.size
                        bestNucleotide = key
                        bestQuality = calculateMedianVale(value)
                    }
                }
            }

            nucleotideSequence.add(Nucleotide(bestNucleotide, bestQuality))
        }

        return Region(
            role,
            nucleotideSequence.map { it.nucleotide }.joinToString(separator = ""),
            nucleotideSequence.map { it.quality }.toTypedArray(),
            feature.gene,
            feature.chromosome,
            feature.start,
            feature.end
        )
    }

    private fun getNucleotideDistribution(
        records: List<SAMRecord>,
        position: Int
    ): Map<Char, List<Byte>> {
        val dist: MutableMap<Char, MutableList<Byte>> = HashMap()
        for (nucleotide in ALLOWED_NUCLEOTIDES) {
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