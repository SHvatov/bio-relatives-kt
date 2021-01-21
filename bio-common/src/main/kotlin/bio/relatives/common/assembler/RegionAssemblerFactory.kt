package bio.relatives.common.assembler

import java.nio.file.Path
import java.util.UUID

/**
 * @author shvatov
 */
interface RegionAssemblerFactory {
    fun create(personIdentifier: UUID, bamFilePath: Path): RegionAssembler
}