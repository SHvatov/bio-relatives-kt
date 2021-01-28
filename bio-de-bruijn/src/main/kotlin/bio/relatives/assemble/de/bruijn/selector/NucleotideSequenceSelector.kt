package bio.relatives.assemble.de.bruijn.selector

import bio.relatives.assemble.de.bruijn.model.sequence.NucleotideSequence
import bio.relatives.common.model.Feature

/**
 * Select the best nucleotide sequence
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
interface NucleotideSequenceSelector {
    fun select(sequences: List<NucleotideSequence>, feature: Feature): NucleotideSequence
}