package bio.relatives.assemble.de.bruijn.builder

import bio.relatives.assemble.de.bruijn.model.Graph
import bio.relatives.common.model.Feature
import htsjdk.samtools.SAMRecord

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
interface GraphBuilder {
    fun buildGraph(feature: Feature, records: List<SAMRecord>): Graph
}