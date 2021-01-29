package bio.relatives.assemble.de.bruijn.analyzer

import bio.relatives.assemble.de.bruijn.model.graph.DeBruijnGraph
import bio.relatives.assemble.de.bruijn.model.graph.Edge
import bio.relatives.assemble.de.bruijn.model.graph.Node
import bio.relatives.assemble.de.bruijn.model.sequence.NucleotideSequence

/**
 * @author Created by Vladislav Marchenko on 26.01.2021
 */
class DeBruijnGraphAnalyzerImpl : DeBruijnGraphAnalyzer {
    override fun analyze(deBruijnGraph: DeBruijnGraph): List<NucleotideSequence> {
        var nucleotideSequences: MutableList<NucleotideSequence> = ArrayList()
        for (startNode in deBruijnGraph.startNodes) {
            val currentGraph = deBruijnGraph.graph

            val currentNucleotides = StringBuilder()
            val currentQualities: MutableList<Byte> = ArrayList()
            val currentPaths = getCurrentPaths(currentGraph, startNode)

            currentPaths.forEach {
                nucleotideSequences = goOnPath(currentGraph, startNode, currentNucleotides, currentQualities, it, nucleotideSequences, true)
            }
        }
        return nucleotideSequences
    }

    private fun goOnPath(
            currentGraph: Map<Node, List<Edge>>,
            currentNode: Node,
            currentNucleotides: StringBuilder,
            currentQualities: MutableList<Byte>,
            currentPath: Edge,
            nucleotideSequences: MutableList<NucleotideSequence>,
            isStartNode: Boolean
    ): MutableList<NucleotideSequence> {
        var myCurrentNode = currentNode
        var resNucleotideSequences = nucleotideSequences

        if (isStartNode) {
            currentNucleotides
                    .append(myCurrentNode.kMer)

            currentQualities.addAll(myCurrentNode.qualities)
        }

        currentNucleotides.append(currentPath.kMer[currentPath.kMer.length - 1])
        currentQualities.add(currentPath.suffix.qualities[currentPath.suffix.qualities.size - 1])
        currentPath.isVisited = true
        myCurrentNode = currentPath.suffix

        if (!currentGraph.containsKey(myCurrentNode)) {
            resNucleotideSequences.add(NucleotideSequence(currentNucleotides.toString(), currentQualities))
        } else {
            val currentPaths = getCurrentPaths(currentGraph, myCurrentNode)

            if (currentPaths.isEmpty()) {
                resNucleotideSequences.add(NucleotideSequence(currentNucleotides.toString(), currentQualities))
            } else {
                currentPaths.forEach {
                    resNucleotideSequences = goOnPath(currentGraph, myCurrentNode, currentNucleotides, currentQualities, it, resNucleotideSequences, false)
                }
            }
        }
        return resNucleotideSequences
    }

    private fun getCurrentPaths(graph: Map<Node, List<Edge>>, node: Node): List<Edge> = (graph[node]
            ?: error("There is not node $node in graph $graph")).filter { !it.isVisited }
    /*var nucleotideSequences: MutableList<NucleotideSequence> = ArrayList()
    for (startNode in deBruijnGraph.startNodes) {
        val graph = deBruijnGraph.graph
        val otherWays: MutableMap<Node, TMPNucleotideSequence> = HashMap()
        var currentNode: Node

        var currentNucleotides = StringBuilder()
        var currentQualities: MutableList<Byte> = ArrayList()

        currentNucleotides.append(startNode.kMer)
        currentQualities.addAll(startNode.qualities)

        var currentWays = getCurrentWays(graph, startNode)

        if (currentWays.size > 1) {
            otherWays[startNode] = TMPNucleotideSequence(currentNucleotides, currentQualities)
        }

        currentNode = currentWays[0].suffix
        currentNucleotides.append(currentWays[0].suffix.kMer[deBruijnGraph.k - 1])
        currentQualities.add(currentWays[0].suffix.qualities[deBruijnGraph.k - 1])
        currentWays[0].isVisited = true

        while (true) {
            currentWays = getCurrentWays(graph, currentNode)

            if (currentWays.isEmpty()) {
                nucleotideSequences.add(NucleotideSequence(currentNucleotides.toString(), currentQualities))
                if (otherWays.isEmpty()) {
                    break
                } else {
                    currentNode = otherWays.keys.first()
                    currentNucleotides = otherWays[currentNode]!!.nucleotides
                    currentQualities = otherWays[currentNode]!!.qualities
                    otherWays.remove(currentNode)
                    continue
                }
            } else {
                if (currentWays.size > 1) {
                    otherWays[currentNode] = TMPNucleotideSequence(currentNucleotides, currentQualities)
                }

                currentNode = currentWays[0].suffix
                currentNucleotides.append(currentWays[0].suffix.kMer[deBruijnGraph.k - 1])
                currentQualities.add(currentWays[0].suffix.qualities[deBruijnGraph.k - 1])
                currentWays[0].isVisited = true
            }
        }
    }
    return nucleotideSequences
}*/

    /* private fun getCurrentWays(graph: Map<Node, List<Edge>>, node: Node): List<Edge> = (graph[node]
             ?: error("There is not node $node in graph $graph")).filter { !it.isVisited }

     data class TMPNucleotideSequence(
             var nucleotides: StringBuilder,
             var qualities: MutableList<Byte>
     )*/
}