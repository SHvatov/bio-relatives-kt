package bio.relatives.common.comparator

/**
 * @author shvatov
 */
interface CompareCtx {
    /**
     * Method, which will be used for the comparison of the genomes.
     */
    val comparisonMethod: GenomeComparisonMethod
}