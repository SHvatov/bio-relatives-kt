package bio.relatives.common.assembler

import bio.relatives.common.model.RegionBatch
import bio.relatives.common.processor.CoroutineParentScopeAware
import bio.relatives.common.processor.CoroutineScopeAware
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeAssembler : AutoCloseable, CoroutineParentScopeAware, CoroutineScopeAware {
    /**
     * Assembles the genome based on the files provided on initialization.
     * Starts a separate coroutine, which is responsible for the assembly of the genomes.
     * Returns a [ReceiveChannel], which contains [RegionBatch], which represent
     * the result of assembly of one region. All regions represent the whole genome.
     */
    fun assemble(): ReceiveChannel<RegionBatch>
}