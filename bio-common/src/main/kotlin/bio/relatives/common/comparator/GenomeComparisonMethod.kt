package bio.relatives.common.comparator

import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region

/**
 * @author shvatov
 */
interface GenomeComparisonMethod {
    /**
     * Compares provided [Region] instances and returns the result as [ComparisonAlgorithmResult].
     */
    fun compare(feature: Feature, left: Region, right: Region): ComparisonAlgorithmResult
}