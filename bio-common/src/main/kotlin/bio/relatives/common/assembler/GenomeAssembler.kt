package bio.relatives.common.assembler

import bio.relatives.common.model.Feature
import bio.relatives.common.model.RegionBatch
import com.shvatov.processor.CoroutineParentScopeAware
import com.shvatov.processor.CoroutineScopeAware
import com.shvatov.processor.data.Task
import com.shvatov.processor.data.TaskResult
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeAssembler : AutoCloseable, CoroutineScopeAware, CoroutineParentScopeAware {
    /**
     * Assembles the genome based on the files provided on initialization.
     * Starts a separate coroutine, which is responsible for the assembly of the genomes.
     * Returns a [ReceiveChannel], which contains [RegionBatch], which represent
     * the result of assembly of one region. All regions represent the whole genome.
     */
    fun assemble(): ReceiveChannel<GenomeAssemblyResult>
}

/**
 * Type aliases for the genome assembly task and its result.
 */
typealias GenomeAssemblyTask = Task<Feature, RegionBatch>
typealias GenomeAssemblyResult = TaskResult<RegionBatch>