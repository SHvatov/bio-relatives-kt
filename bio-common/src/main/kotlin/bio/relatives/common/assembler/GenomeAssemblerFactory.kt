package bio.relatives.common.assembler

import java.nio.file.Path

/**
 * @author shvatov
 */
interface GenomeAssemblerFactory {
    fun create(featureFilePath: Path, vararg bamFilePaths: Path): GenomeAssembler
}