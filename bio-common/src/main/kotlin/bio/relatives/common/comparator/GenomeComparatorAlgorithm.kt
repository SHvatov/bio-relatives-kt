package bio.relatives.common.comparator

import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region

/**
 * Represents comparison algorithm, which can be used for genome comparison.
 * @author shvatov
 */
interface GenomeComparatorAlgorithm {
    /**
     * Compares provided [Region] instances and returns the result as [ComparisonAlgorithmResult].
     */
    fun compare(left: Region, right: Region, feature: Feature): ComparisonAlgorithmResult
}