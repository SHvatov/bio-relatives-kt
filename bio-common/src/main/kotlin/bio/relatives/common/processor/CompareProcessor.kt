package bio.relatives.common.processor

import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.model.ComparisonParticipants
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult
import bio.relatives.common.model.RegionBatch
import kotlinx.coroutines.*

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CompareProcessor(
    private val compareCtx: CompareCtx,
    parentScope: CoroutineScope
) : AbstractProcessor<RegionBatch, ComparisonResult>(parentScope) {
    override fun makeSubProcessor() = object : AbstractSubProcessor() {
        override suspend fun process(
                parentScope: CoroutineScope,
                batch: RegionBatch
        ): ComparisonResult {
            val results =
                mutableMapOf<ComparisonParticipants, Deferred<ComparisonAlgorithmResult>>()
            for (between in ComparisonParticipants.COMPARISON_PAIRS) {
                val (first, second) = batch.regionsByRole[between.firstRole] to batch.regionsByRole[between.secondRole]
                if (first != null && second != null) {
                    results[between] = parentScope.async {
                        compareCtx.algorithm.compare(batch.feature, first, second)
                    }
                }
            }

            return results.mapValuesTo(ComparisonResult()) { (_, deferred) -> deferred.await() }
        }
    }
}