package bio.relatives.common.assembler

import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware
import bio.relatives.common.model.RoleAware.Role
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeAssembler : AutoCloseable {
    /**
     * Assembles the genome based on the files provided on initialization.
     * Starts a separate coroutine, which is responsible for the assembly of the genomes.
     * Returns a [ReceiveChannel], which contains [RegionBatch], which represent
     * the result of assembly of one region. All regions represent the whole genome.
     */
    fun assemble(): ReceiveChannel<RegionBatch>
}

/**
 * @note: This is a basic alias for map, where key is a [RoleAware.Role] of the
 * person, whose genome is being parsed, while [Region] represents genome itself.
 */
typealias RegionBatch = HashMap<Role, Region>