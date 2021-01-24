package bio.relatives.assemble.naive.assembler

import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import htsjdk.samtools.SAMRecord
import java.util.*

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
class NaiveRegionAssembler : RegionAssembler {

    private val NUCLEOTIDES = "agct"


    private val UNKNOWN_NUCLEOTIDE = '*'


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
        return Region(nucleotideSequence.toString(), feature.chromosome, feature.gene, feature.start, feature.end)
    }

    private fun getNucleotideDistribution(records: List<SAMRecord>, position: Long): Map<Char, List<Byte>> {

        val dist: MutableMap<Char, MutableList<Byte>> = HashMap()
        for (nucleotide in NUCLEOTIDES.toCharArray()) {
            dist[nucleotide] = ArrayList()
        }

        for (rec in records) {
            val pos = position - rec.start
            var currentNucleotide = ' '
            var currentQuality: Byte = 0
            if (pos < rec.readLength && pos >= 0) {
                currentNucleotide = Character.toLowerCase(rec.readString[pos.toInt()])
                currentQuality = rec.baseQualities[pos.toInt()]
            }
            if (NUCLEOTIDES.contains(currentNucleotide.toString())) {
                dist[currentNucleotide]!!.add(currentQuality)
            }
        }
        return dist
    }

    private fun getMedianQuality(qualities: List<Byte>): Byte {

        qualities.sorted()

        return when {
            qualities.size % 2 != 0 -> {
                qualities[qualities.size / 2]
            }
            qualities.isNotEmpty() -> {
                ((qualities[qualities.size / 2] + qualities[qualities.size / 2 - 1]) / 2).toByte()
            }
            else -> {
                0
            }
        }
    }
}