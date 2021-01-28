package bio.relatives.assemble.de.bruijn.selector

import bio.relatives.assemble.de.bruijn.model.sequence.NucleotideSequence
import bio.relatives.common.model.Feature
import bio.relatives.common.utils.getMedianQuality

/**
 * @author Created by Vladislav Marchenko on 28.01.2021
 */
class NucleotideSequenceSelectorImpl : NucleotideSequenceSelector {
    override fun select(sequences: List<NucleotideSequence>, feature: Feature): NucleotideSequence = sequences
            .toMutableList()
            .filter { it.nucleotides.length == (feature.end - feature.start + 1) }
            .sortedByDescending { getMedianQuality(it.qualities) }[0]
}