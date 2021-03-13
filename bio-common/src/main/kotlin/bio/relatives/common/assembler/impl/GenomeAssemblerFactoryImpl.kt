package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.assembler.GenomeAssemblerFactory
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@Component("GenomeAssemblerFactory")
class GenomeAssemblerFactoryImpl : GenomeAssemblerFactory {
    override fun create(assemblyCtx: AssemblyCtx): GenomeAssembler = GenomeAssemblerImpl(assemblyCtx)
}