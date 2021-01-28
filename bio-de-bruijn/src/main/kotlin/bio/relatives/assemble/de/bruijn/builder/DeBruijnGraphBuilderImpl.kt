package bio.relatives.assemble.de.bruijn.builder

import bio.relatives.assemble.de.bruijn.model.graph.DeBruijnGraph
import bio.relatives.assemble.de.bruijn.model.graph.Edge
import bio.relatives.assemble.de.bruijn.model.graph.Node
import bio.relatives.common.utils.ALLOWED_NUCLEOTIDES
import bio.relatives.common.utils.UNKNOWN_NUCLEOTIDE
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
                val node1 = buildNode(i, i + kMerSize, currentSequence, currentQualities.toList())

                val node2 = buildNode(i + 1, i + kMerSize + 1, currentSequence, currentQualities.toList())

                val edge = Edge(node1, node2, node1.kMer + node2.kMer[kMerSize - 1], false)

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

    private fun buildNode(start: Int, end: Int, currentSequence: String, currentQualities: List<Byte>): Node {
        val sequence = currentSequence.substring(start, end).toLowerCase()
        val qualities = currentQualities.slice(start until end).toMutableList()
        return if (!sequence.all { ALLOWED_NUCLEOTIDES.contains(it) }) {
            val ourSequence = StringBuilder(sequence)
            sequence.forEachIndexed { index, char ->
                run {
                    if (!ALLOWED_NUCLEOTIDES.contains(char)) {
                        ourSequence.replaceRange(index, index, UNKNOWN_NUCLEOTIDE.toString())
                        qualities[index] = 0
                    }
                }
            }
            Node(ourSequence.toString(), qualities)
        } else {
            Node(sequence, qualities)
        }

    }
}