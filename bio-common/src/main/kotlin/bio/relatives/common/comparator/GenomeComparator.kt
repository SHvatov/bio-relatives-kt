package bio.relatives.common.comparator

import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.RegionBatch

/**
 * @author shvatov
 */
interface GenomeComparator {
    fun compare(regions: List<RegionBatch>): List<ComparisonResult>
}