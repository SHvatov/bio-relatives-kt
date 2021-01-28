package bio.relatives.common.comparator

/**
 * @author shvatov
 */
interface CompareCtx {
    /**
     * Algorithm, which will be used for the comparison of the genomes.
     */
    val algorithm: GenomeComparatorAlgorithm
}