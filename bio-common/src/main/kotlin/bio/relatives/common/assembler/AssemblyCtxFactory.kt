package bio.relatives.common.assembler

import java.nio.file.Path

/**
 * @author shvatov
 */
interface AssemblyCtxFactory {
    /**
     * Creates an instance of [AssemblyCtx], which will be used afterwards for the
     * assembling of the genomes based on the [featureFilePath] and [bamFilePaths].
     * For each [bamFilePaths] generates a unique UUID, this file will be associated with
     * within the system.
     */
    fun create(featureFilePath: Path, vararg bamFilePaths: Path): AssemblyCtx
}