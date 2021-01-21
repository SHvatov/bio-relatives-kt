package bio.relatives.common.assembler

import java.nio.file.Path

/**
 * @author shvatov
 */
interface RegionAssemblerFactory {
    fun create(bamFilePath: Path): RegionAssembler
}