package bio.relatives.common.assembler

import kotlinx.coroutines.CoroutineScope

/**
 * @author shvatov
 */
interface GenomeAssemblerFactory {
    /**
     * Creates a [GenomeAssembler] based on the provided [assemblyCtx] and
     * [parentScope] id such is present.
     */
    fun create(
        assemblyCtx: AssemblyCtx,
        parentScope: CoroutineScope? = null
    ): GenomeAssembler
}