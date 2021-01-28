package bio.relatives.assemble.de.bruijn.model.graph

/**
 * @author Created by Vladislav Marchenko on 21.01.2021
 */
data class Node(
        val kMer: String,
        val qualities: List<Byte>
)