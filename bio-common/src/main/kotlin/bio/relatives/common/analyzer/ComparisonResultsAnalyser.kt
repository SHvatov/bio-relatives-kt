package bio.relatives.common.analyzer

import bio.relatives.common.model.AnalysisResult
import bio.relatives.common.model.ComparisonResult

/**
 * @author shvatov
 */
interface ComparisonResultsAnalyser {
    fun analyse(cmpResults: List<ComparisonResult>): AnalysisResult
}