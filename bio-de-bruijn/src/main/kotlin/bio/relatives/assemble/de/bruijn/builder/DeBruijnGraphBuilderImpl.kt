package bio.relatives.assemble.de.bruijn.builder

import bio.relatives.assemble.de.bruijn.model.graph.DeBruijnGraph
import bio.relatives.assemble.de.bruijn.model.graph.Edge
import bio.relatives.assemble.de.bruijn.model.graph.Node
import htsjdk.samtools.SAMRecord

/**
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
class DeBruijnGraphBuilderImpl : DeBruijnGraphBuilder {
    override fun buildGraph(records: List<SAMRecord>, kMerSize: Int): DeBruijnGraph {
        val nodes: MutableSet<Node> = HashSet()
        val startNodes: MutableList<Node> = ArrayList()
        val graph: MutableMap<Node, MutableList<Edge>> = HashMap()

        for (r in records) {
            val currentSequence = r.readString
            val currentQualities = r.baseQualities

            for (i in 0 until currentSequence.length - kMerSize) {
                val node1 = Node(currentSequence.substring(i, i + kMerSize),
                        currentQualities.slice(i until i + kMerSize))
                val node2 = Node(currentSequence.substring(i + 1, i + kMerSize + 1),
                        currentQualities.slice(i until i + kMerSize + 1))
                val edge = Edge(node1, node2, currentSequence.substring(i..i + kMerSize), false)

                if (!nodes.contains(node1)) {
                    startNodes.add(node1)
                }

                nodes.add(node1)
                nodes.add(node2)

                if (graph.containsKey(node1)) {
                    graph[node1]!!.add(edge)
                } else {
                    graph[node1] = mutableListOf(edge)
                }
            }
        }
        return DeBruijnGraph(graph, startNodes, kMerSize)
    }
}