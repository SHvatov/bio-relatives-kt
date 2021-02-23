package bio.relatives.common.analyzer

import bio.relatives.common.comparator.GenomeComparisonResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface ComparisonResultsAnalyserFactory {
    /**
     * Creates an instance of the [ComparisonResultsAnalyser], which then
     * can be used for the analysis of the comparison result, based on the
     * provided [parentScope] and [inputChannel].
     */
    fun create(
        inputChannel: ReceiveChannel<GenomeComparisonResult>,
        parentScope: CoroutineScope? = null
    ): ComparisonResultsAnalyser
}