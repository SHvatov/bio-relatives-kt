package bio.relatives.common.comparator

import bio.relatives.common.assembler.GenomeAssemblyResult
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.RegionBatch
import com.shvatov.processor.CoroutineParentScopeAware
import com.shvatov.processor.CoroutineScopeAware
import com.shvatov.processor.data.Task
import com.shvatov.processor.data.TaskResult
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeComparator : CoroutineParentScopeAware, CoroutineScopeAware {
    /**
     * Input channel, that will contain the results of the genome assembly
     * step of the algorithm.
     */
    val inputChannel: ReceiveChannel<GenomeAssemblyResult>

    /**
     * Based on the data, provided from [inputChannel], performs the comparison
     * of the input data using specified method. Starts a separate coroutine for that.
     * Returns a [ReceiveChannel] of [GenomeComparisonResult].
     */
    fun compare(): ReceiveChannel<GenomeComparisonResult>
}

/**
 * Type aliases for the genome assembly task and its result.
 */
typealias GenomeComparisonTask = Task<RegionBatch, ComparisonResult>
typealias GenomeComparisonResult = TaskResult<ComparisonResult>