package bio.relatives.common.assembler

/**
 * @author shvatov
 */
interface GenomeAssemblerFactory {
    fun create(assemblyCtx: AssemblyCtx): GenomeAssembler
}