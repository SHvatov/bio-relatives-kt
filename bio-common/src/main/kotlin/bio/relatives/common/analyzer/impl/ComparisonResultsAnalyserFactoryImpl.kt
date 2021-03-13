package bio.relatives.common.analyzer.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyser
import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@Component("ComparisonResultsAnalyzerFactory")
class ComparisonResultsAnalyserFactoryImpl : ComparisonResultsAnalyserFactory {
    override fun create(): ComparisonResultsAnalyser = ComparisonResultsAnalyserImpl()
}