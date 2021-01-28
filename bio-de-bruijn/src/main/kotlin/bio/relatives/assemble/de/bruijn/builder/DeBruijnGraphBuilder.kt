package bio.relatives.assemble.de.bruijn.builder

import bio.relatives.assemble.de.bruijn.model.graph.DeBruijnGraph
import htsjdk.samtools.SAMRecord

/**
 * Build DeBruijn graph from input list of SAMRecords
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
interface DeBruijnGraphBuilder {
    fun buildGraph(records: List<SAMRecord>, kMerSize: Int): DeBruijnGraph
}