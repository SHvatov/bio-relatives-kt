package bio.relatives.common.model

/**
 * @author shvatov
 */
interface NucleotideSequenceAware {
    /**
     * Genome sequnce itself.
     */
    val sequence: String

    /**
     * Array of the qualities, that corresponds to the quality of the
     * nucleotide read by the sequenator. Potentially could be used
     * for calculating the error rate.
     */
    val qualities: Array<Byte>
}