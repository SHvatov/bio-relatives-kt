package bio.relatives.assemble.de.bruijn.model.graph

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
data class DeBruijnGraph(
        val graph: Map<Node, List<Edge>>,
        val startNodes: List<Node>,
        val k: Int
)

