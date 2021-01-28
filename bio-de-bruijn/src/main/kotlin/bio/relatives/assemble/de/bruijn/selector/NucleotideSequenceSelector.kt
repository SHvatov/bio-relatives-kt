package bio.relatives.assemble.de.bruijn.selector

import bio.relatives.assemble.de.bruijn.model.sequence.NucleotideSequence
import bio.relatives.common.model.Feature

/**
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
interface NucleotideSequenceSelector {
    /**
     * Select the best nucleotide sequence
     */
    fun select(sequences: List<NucleotideSequence>, feature: Feature): NucleotideSequence
}