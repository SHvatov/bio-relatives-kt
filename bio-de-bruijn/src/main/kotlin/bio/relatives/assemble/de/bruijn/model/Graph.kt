package bio.relatives.assemble.de.bruijn.model

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
data class Graph(
        val edges: List<Edge>,
        val startEdge: Edge
)

