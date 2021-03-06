package bio.relatives.common.assembler

import bio.relatives.common.model.RoleAware.Role
import java.nio.file.Path

/**
 * @author shvatov
 */
interface AssemblyCtxFactory {
    /**
     * Creates an instance of [AssemblyCtx], which will be used afterwards for the
     * assembling of the genomes based on the provided [featureFilePath] and [bamFilePaths]
     * and injected components.
     */
    fun create(featureFilePath: Path, bamFilePaths: Map<Role, Path>): AssemblyCtx
}