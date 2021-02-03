package bio.relatives.common.analyzer.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyser
import bio.relatives.common.analyzer.impl.ComparisonResultsAnalyserImpl.AnalyserCommand.PerformAnalysis
import bio.relatives.common.analyzer.impl.ComparisonResultsAnalyserImpl.AnalyserCommand.StoreResult
import bio.relatives.common.model.AnalysisResult
import bio.relatives.common.model.AnalysisResult.ChromosomeResult
import bio.relatives.common.model.AnalysisResult.GenomeResult
import bio.relatives.common.model.ComparisonParticipants
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult
import bio.relatives.common.utils.calculateAdditionRelativeErrorRate
import bio.relatives.common.utils.calculateAverageQuality
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ComparisonResultsAnalyserImpl : ComparisonResultsAnalyser {
    /**
     * Parent scope for the execution of analysing coroutine.
     */
    private val scope = CoroutineScope(
        SupervisorJob() +
            CoroutineName("Analyzer") +
            Executors.newSingleThreadExecutor().asCoroutineDispatcher() +
            CoroutineExceptionHandler { ctx, exception ->
                LOG.error(
                    "Exception occurred while processing data in ${ctx[CoroutineName]}:",
                    exception
                )
            }
    )

    /**
     * Actor, which is responsible for the storage and analysis of the comparison data.
     */
    private val analyser = scope.actor<AnalyserCommand>(
        capacity = CHANNEL_CAPACITY
    ) {
        val storedResults =
            mutableMapOf<ComparisonParticipants, MutableList<ComparisonAlgorithmResult>>()
        consumeEach {
            when (it) {
                is StoreResult -> with(it) {
                    result.forEach { (participants, algorithmResult) ->
                        storedResults.putIfAbsent(participants, mutableListOf())
                        storedResults.getValue(participants).add(algorithmResult)
                    }
                }
                is PerformAnalysis ->
                    it.result.complete(
                        performAnalysis(storedResults)
                    )
            }
        }
    }

    override fun store(result: ComparisonResult) {
        scope.launch {
            analyser.send(StoreResult(result))
        }
    }

    override fun analyse(): AnalysisResult = runBlocking {
        val deferredResult = CompletableDeferred<AnalysisResult>()
        analyser.send(PerformAnalysis(deferredResult))
        return@runBlocking deferredResult.await()
    }

    override fun close() {
        analyser.close()
        scope.cancel()
    }

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

    /**
     * Defines the hierarchy of sealed classes, that represent the tasks,
     * that [ComparisonResultsAnalyser] inner actor [analyser] supports.
     */
    private sealed class AnalyserCommand {
        /**
         * Adds the result to the coroutine-local synchronized map.
         */
        class StoreResult(val result: ComparisonResult) : AnalyserCommand()

        /**
         * Starts the analysis of the accumulated data.
         * Returns the result in [CompletableDeferred].
         */
        class PerformAnalysis(val result: CompletableDeferred<AnalysisResult>) : AnalyserCommand()
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(ComparisonResultsAnalyser::class.java)

        const val CHANNEL_CAPACITY = 10
    }
}