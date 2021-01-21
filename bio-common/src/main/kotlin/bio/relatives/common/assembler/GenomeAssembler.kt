package bio.relatives.common.assembler

import bio.relatives.common.model.Region
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.UUID

/**
 * @author shvatov
 */
interface GenomeAssembler : AutoCloseable {
    fun assemble(): ReceiveChannel<RegionBatch>
}

/**
 * @note: This is a basic alias for map, where key is a unique identifier of the
 * person, whose genome is being parsed, while [Region] represents genome itself.
 */
typealias RegionBatch = HashMap<UUID, Region>