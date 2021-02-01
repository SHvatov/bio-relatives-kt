package bio.relatives.common.model

import bio.relatives.common.model.AnalysisResult.GenomeResult

/**
 * @author shvatov
 */
class AnalysisResult : MutableMap<ComparisonParticipants, GenomeResult> by HashMap() {
    /**
     * Result of the comparison of the whole genomes. Stores the [averageSimilarity]
     * and [averageErrorRate] of the genomes. Also stores some detailed information
     * about each chromosome.
     */
    data class GenomeResult(
        val averageSimilarity: Double,
        val averageErrorRate: Double,
        val chromosomeResults: List<ChromosomeResult>
    )

    /**
     * Result of the comparison of two chromosomes. Stores the [averageSimilarity]
     * and [averageErrorRate] of the chromosomes.
     */
    data class ChromosomeResult(
        override val gene: String,
        override val chromosome: String,
        val averageSimilarity: Double,
        val averageErrorRate: Double,
    ) : ChromosomeAware
}