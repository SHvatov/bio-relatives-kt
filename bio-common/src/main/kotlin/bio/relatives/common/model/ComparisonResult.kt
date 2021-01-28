package bio.relatives.common.model

import bio.relatives.common.model.ComparisonResult.AlgorithmResult
import bio.relatives.common.model.ComparisonResult.Between
import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.model.RoleAware.Role.FATHER
import bio.relatives.common.model.RoleAware.Role.MOTHER
import bio.relatives.common.model.RoleAware.Role.SON

/**
 * Represents the result of the comparison of two or more genomes between each other.
 * @author shvatov
 */
class ComparisonResult : MutableMap<Between, AlgorithmResult> by HashMap() {
    /**
     * Defines the roles, between which the comparison has happened.
     */
    abstract class Between(
        val firstRole: Role,
        val secondRole: Role
    ) {
        /**
         * Comparison between mother and son.
         */
        object MotherAndSon : Between(MOTHER, SON)

        /**
         * Comparison between father and son.
         */
        object FatherAndSon : Between(FATHER, SON)

        companion object {
            val COMPARISON_PAIRS = listOf(MotherAndSon, FatherAndSon)
        }
    }

    /**
     * Interface of the result of the comparison, produced by one of the algorithms.
     */
    interface AlgorithmResult {
        /**
         * Result, produced by the algorithm. For example, the similarity percent.
         */
        val result: Any

        /**
         * Some additional info, which can be used by the results analyzer.
         */
        val additionalInfo: Map<String, Any>
    }
}