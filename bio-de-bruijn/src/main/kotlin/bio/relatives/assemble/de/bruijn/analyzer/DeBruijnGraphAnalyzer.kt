package bio.relatives.assemble.de.bruijn.analyzer

import bio.relatives.assemble.de.bruijn.model.graph.DeBruijnGraph
import bio.relatives.assemble.de.bruijn.model.sequence.NucleotideSequence

/**
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
interface DeBruijnGraphAnalyzer {
    /**
     * Analyze input DeBruijn graph. Find all probability nucleotide sequences.
     */
    fun analyze(deBruijnGraph: DeBruijnGraph): List<NucleotideSequence>
}