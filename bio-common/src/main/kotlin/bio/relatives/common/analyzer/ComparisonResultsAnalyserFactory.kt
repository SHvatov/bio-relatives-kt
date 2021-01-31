package bio.relatives.common.analyzer

/**
 * @author shvatov
 */
interface ComparisonResultsAnalyserFactory {
    /**
     * Creates an instance of the [ComparisonResultsAnalyser], which then
     * can be used for the analysis of the comparison result.
     */
    fun create(): ComparisonResultsAnalyser
}