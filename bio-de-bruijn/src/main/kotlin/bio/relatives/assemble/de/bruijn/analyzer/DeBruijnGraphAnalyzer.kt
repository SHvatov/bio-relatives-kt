package bio.relatives.assemble.de.bruijn.analyzer

import bio.relatives.assemble.de.bruijn.model.graph.DeBruijnGraph
import bio.relatives.assemble.de.bruijn.model.sequence.NucleotideSequence

/**
 * Analyze input DeBruijn graph. Find all probability nucleotide sequences.
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
interface DeBruijnGraphAnalyzer {
    fun analyze(deBruijnGraph: DeBruijnGraph): List<NucleotideSequence>
}