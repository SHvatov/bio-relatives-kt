package bio.relatives.common.analyzer

import bio.relatives.common.model.AnalysisResult
import bio.relatives.common.model.ComparisonResult

/**
 * @author shvatov
 */
interface ComparisonResultsAnalyser : AutoCloseable {
    /**
     * Stores the result of the comparison in local storage.
     */
    fun store(result: ComparisonResult)

    /**
     * Performs the analysis of the results, that have been accumulated
     * at the given point.
     */
    fun analyse(): AnalysisResult
}