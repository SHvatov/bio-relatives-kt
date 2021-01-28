package bio.relatives.common.comparator

import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.ComparisonResult.AlgorithmResult
import bio.relatives.common.model.Region

/**
 * Represents comparison algorithm, which can be used for genome comparison.
 * @author shvatov
 */
interface GenomeComparatorAlgorithm {
    /**
     * Compares provided [Region]  instances and returns some kind of the result,
     * which implements [ComparisonResult.AlgorithmResult] interface.
     */
    fun compare(left: Region, right: Region): AlgorithmResult
}