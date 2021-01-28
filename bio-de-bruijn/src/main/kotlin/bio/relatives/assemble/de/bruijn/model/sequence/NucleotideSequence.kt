package bio.relatives.assemble.de.bruijn.model.sequence

/**
 * @author Created by Vladislav Marchenko on 24.01.2021
 */
data class NucleotideSequence(
        val nucleotides: String,
        val qualities: List<Byte>
)