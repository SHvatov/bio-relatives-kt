package bio.relatives.common.analyzer.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyser
import bio.relatives.common.model.AnalysisResult
import bio.relatives.common.model.AnalysisResult.ChromosomeResult
import bio.relatives.common.model.AnalysisResult.GenomeResult
import bio.relatives.common.model.ComparisonParticipants
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult
import bio.relatives.common.utils.calculateAdditionRelativeErrorRate
import bio.relatives.common.utils.calculateAverageQuality

/**
 * @author shvatov
 */
class ComparisonResultsAnalyserImpl : ComparisonResultsAnalyser {
    override fun analyse(cmpResults: List<ComparisonResult>): AnalysisResult {
        val cmpResultsByPtcp = cmpResults.asSequence()
            .flatMap { it.entries }
            .groupBy { it.key }
            .mapValues { (_, values) -> values.map { it.value } }
        return performAnalysis(cmpResultsByPtcp)
    }

    /**
     * Analyses the [storedResults] for each pair of roles asynchronously and returns
     * the result after all the routines are finished.
     */
    private fun performAnalysis(
        storedResults: Map<ComparisonParticipants, List<ComparisonAlgorithmResult>>
    ): AnalysisResult {
        fun performSingleAnalysis(resultsByParticipants: List<ComparisonAlgorithmResult>): GenomeResult {
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

        val analysisResults = AnalysisResult()
        for ((participants, algorithmResults) in storedResults) {
            analysisResults[participants] = performSingleAnalysis(algorithmResults.filter { it.errorRate > 0.0 })
        }

        return analysisResults
    }
}