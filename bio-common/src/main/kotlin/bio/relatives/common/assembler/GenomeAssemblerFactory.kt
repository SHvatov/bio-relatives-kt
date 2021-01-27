package bio.relatives.common.assembler

import bio.relatives.common.model.RoleAware.Role
import java.nio.file.Path

/**
 * @author shvatov
 */
interface GenomeAssemblerFactory {
    fun create(featureFilePath: Path, bamFilePaths: Map<Role, Path>): GenomeAssembler
}