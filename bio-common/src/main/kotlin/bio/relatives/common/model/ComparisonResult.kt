package bio.relatives.common.model

import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult

/**
 * Represents the result of the comparison of two or more genomes between each other.
 * @author shvatov
 */
class ComparisonResult :
    MutableMap<ComparisonParticipants, ComparisonAlgorithmResult> by HashMap() {
    /**
     * @author shvatov
     */
    data class ComparisonAlgorithmResult(
        /**
         * Feature, which has been assembled and compared between two persons.
         */
        val feature: Feature,

        /**
         * Percentage of the similarity between two genomes according to the algorithm.
         */
        val similarityPercentage: Double,

        /**
         * Error rate of the algorithm based on the assembly error rate.
         */
        val errorRate: Double
    )
}