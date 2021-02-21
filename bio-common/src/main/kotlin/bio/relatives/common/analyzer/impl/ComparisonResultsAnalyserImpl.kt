package bio.relatives.common.analyzer.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyser
import bio.relatives.common.comparator.GenomeComparisonResult
import bio.relatives.common.model.AnalysisResult
import bio.relatives.common.model.AnalysisResult.ChromosomeResult
import bio.relatives.common.model.AnalysisResult.GenomeResult
import bio.relatives.common.model.ComparisonParticipants
import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult
import bio.relatives.common.utils.calculateAdditionRelativeErrorRate
import bio.relatives.common.utils.calculateAverageQuality
import com.shvatov.processor.CoroutineScopeAware
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ComparisonResultsAnalyserImpl(
    /**
     * Input channel, that will contain the results of the genome comparison
     * step of the algorithm.
     */
    override val inputChannel: ReceiveChannel<GenomeComparisonResult>,

    /**
     * Scope of the parent coroutine this assembler is called from.
     */
    override val parentScope: CoroutineScope? = null
) : ComparisonResultsAnalyser {
    /**
     * Parent scope for the execution of analysing coroutine.
     */
    override val scope = CoroutineScope(
        (parentScope?.coroutineContext ?: EmptyCoroutineContext) +
            CoroutineName("Analyzer") +
            Executors.newSingleThreadExecutor().asCoroutineDispatcher() +
            CoroutineScopeAware.exceptionHandler(LOG)
    )

    /**
     * Deferred value, which will contain the results of the analysis
     * after all data from [inputChannel] has been processed.
     */
    private val analysisResult: CompletableDeferred<AnalysisResult> = CompletableDeferred()

    init {
        /**
         * On init we start a separate coroutine, which consumes data from `inputChannel` till the very end,
         * and after that completes `analysisResult` with the value computed by `performAnalysis` method.
         */
        scope.launch {
            val storedResults = mutableMapOf<ComparisonParticipants, MutableList<ComparisonAlgorithmResult>>()
            inputChannel.consumeEach {
                with(it) {
                    if (it.isSuccess) {
                        result!!.forEach { (participants, algorithmResult) ->
                            storedResults.putIfAbsent(participants, mutableListOf())
                            storedResults.getValue(participants).add(algorithmResult)
                        }
                    }
                }
            }

            analysisResult.complete(
                performAnalysis(storedResults)
            )
        }
    }

    override suspend fun analyse(): AnalysisResult = analysisResult.await()

    /**
     * Analyses the [storedResults] for each pair of roles asynchronously and returns
     * the result after all the routines are finished.
     */
    private suspend fun performAnalysis(
        storedResults: Map<ComparisonParticipants, List<ComparisonAlgorithmResult>>
    ): AnalysisResult {
        fun performAnalysisAsync(resultsByParticipants: List<ComparisonAlgorithmResult>): GenomeResult {
            val resultsByChromosomeAndGene = resultsByParticipants.groupBy {
                with(it.feature) {
                    gene to chromosome
                }
            }

            val chromosomeAnalysisResults = mutableListOf<ChromosomeResult>()
            for ((key, result) in resultsByChromosomeAndGene) {
                val chromosomeAverageSimilarity =
                    calculateAverageQuality(result.map { it.similarityPercentage })
                val chromosomeRelativeErrorRate =
                    calculateAdditionRelativeErrorRate(
                        result.associate { it.similarityPercentage to it.errorRate }
                    ) / 2
                val chromosomeAbsoluteErrorRate =
                    chromosomeRelativeErrorRate * chromosomeAverageSimilarity

                chromosomeAnalysisResults.add(
                    ChromosomeResult(
                        key.first,
                        key.second,
                        chromosomeAverageSimilarity,
                        chromosomeAbsoluteErrorRate
                    )
                )
            }

            val genomeAverageSimilarity =
                calculateAverageQuality(chromosomeAnalysisResults.map { it.averageSimilarity })
            val genomeRelativeErrorRate =
                calculateAdditionRelativeErrorRate(
                    chromosomeAnalysisResults.associate { it.averageSimilarity to it.averageErrorRate }
                ) / 2
            val genomeAbsoluteErrorRate = genomeRelativeErrorRate * genomeAverageSimilarity
            return GenomeResult(
                genomeAverageSimilarity,
                genomeAbsoluteErrorRate,
                chromosomeAnalysisResults
            )
        }

        val deferredAnalysisResults = mutableMapOf<ComparisonParticipants, Deferred<GenomeResult>>()
        for ((participants, algorithmResults) in storedResults) {
            deferredAnalysisResults[participants] = scope.async {
                performAnalysisAsync(algorithmResults)
            }
        }

        return deferredAnalysisResults.mapValuesTo(AnalysisResult()) { (_, value) ->
            value.await()
        }
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(ComparisonResultsAnalyser::class.java)
    }
}