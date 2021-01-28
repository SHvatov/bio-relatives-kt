package bio.relatives.assemble.de.bruijn.model.graph

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
data class Edge(
        val prefix: Node,
        val suffix: Node,
        val kMer: String,
        var isVisited: Boolean
)