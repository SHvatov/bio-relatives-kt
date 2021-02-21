package bio.relatives.common.analyzer.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyser
import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import bio.relatives.common.comparator.GenomeComparisonResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Component("ComparisonResultsAnalyzerFactory")
class ComparisonResultsAnalyserFactoryImpl : ComparisonResultsAnalyserFactory {
    override fun create(
        inputChannel: ReceiveChannel<GenomeComparisonResult>,
        parentScope: CoroutineScope?
    ): ComparisonResultsAnalyser = ComparisonResultsAnalyserImpl(inputChannel, parentScope)
}