package bio.relatives.common.assembler

import bio.relatives.common.model.RoleAware.Role
import kotlinx.coroutines.CoroutineScope
import java.nio.file.Path

/**
 * @author shvatov
 */
interface GenomeAssemblerFactory {
    fun create(
        featureFilePath: Path,
        bamFilePaths: Map<Role, Path>,
        parentScope: CoroutineScope
    ): GenomeAssembler
}