package bio.relatives.common.comparator

/**
 * @author shvatov
 */
interface CompareCtxFactory {
    /**
     * Creates an instance of the [CompareCtx] based on the [GenomeComparisonMethod],
     * injected from application context.
     */
    fun create(): CompareCtx
}