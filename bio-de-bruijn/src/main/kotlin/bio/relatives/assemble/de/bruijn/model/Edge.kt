package bio.relatives.assemble.de.bruijn.model

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
data class Edge(
        var prefix: Edge,
        var suffix: Edge,
        var value: String
)