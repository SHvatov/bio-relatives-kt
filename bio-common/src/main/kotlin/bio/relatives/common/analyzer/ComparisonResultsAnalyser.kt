package bio.relatives.common.analyzer

import bio.relatives.common.comparator.GenomeComparisonResult
import bio.relatives.common.model.AnalysisResult
import com.shvatov.processor.CoroutineParentScopeAware
import com.shvatov.processor.CoroutineScopeAware
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface ComparisonResultsAnalyser : CoroutineParentScopeAware, CoroutineScopeAware {
    /**
     * Input channel, that will contain the results of the genome comparison
     * step of the algorithm.
     */
    val inputChannel: ReceiveChannel<GenomeComparisonResult>

    /**
     * Performs the analysis of the results, that have been accumulated
     * after processing all the data from [inputChannel]. Suspends current
     * coroutine till that moment.
     */
    suspend fun analyse(): AnalysisResult
}