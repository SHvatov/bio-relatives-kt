package bio.relatives.common.analyzer.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyser
import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Component("ComparisonResultsAnalyzerFactory")
class ComparisonResultsAnalyserFactoryImpl : ComparisonResultsAnalyserFactory {
    override fun create(): ComparisonResultsAnalyser = ComparisonResultsAnalyserImpl()
}