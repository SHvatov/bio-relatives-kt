package bio.relatives.assemble.de.bruijn.selector

import bio.relatives.assemble.de.bruijn.model.sequence.NucleotideSequence
import bio.relatives.common.model.Feature

/**
 * @author Created by Vladislav Marchenko on 28.01.2021
 */
class NucleotideSequenceSelectorImpl : NucleotideSequenceSelector {
    override fun select(sequences: List<NucleotideSequence>, feature: Feature): NucleotideSequence = sequences
            .toMutableList()
            .filter { it.nucleotides.length == (feature.end - feature.start + 1) }
            .sortedByDescending { getMedianQuality(it.qualities) }[0]


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