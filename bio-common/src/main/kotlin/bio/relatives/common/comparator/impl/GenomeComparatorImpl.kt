package bio.relatives.common.comparator.impl

import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.model.ComparisonParticipants
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.RegionBatch

/**
 * @author shvatov
 */
class GenomeComparatorImpl(
    /**
     * Comparison context, which contains singletons and some additional data,
     * required for the proper work of the comparison algorithms on lower levels.
     */
    private val compareCtx: CompareCtx,
) : GenomeComparator {
    override fun compare(regions: List<RegionBatch>): List<ComparisonResult> {
        return regions.map { compareRegions(it) }
    }

    /**
     * Performs synchronized comparison of the regions from [regionBatch].
     */
    private fun compareRegions(regionBatch: RegionBatch): ComparisonResult {
        return ComparisonParticipants.COMPARISON_PAIRS
            .associateWith { regionBatch.regionsByRole[it.firstRole] to regionBatch.regionsByRole[it.secondRole] }
            .filter { (_, value) -> value.first != null && value.second != null }
            .mapValues { (_, value) ->
                compareCtx.comparisonMethod.compare(
                    regionBatch.feature,
                    value.first!!,
                    value.second!!
                )
            }.toMap(ComparisonResult())
    }
}