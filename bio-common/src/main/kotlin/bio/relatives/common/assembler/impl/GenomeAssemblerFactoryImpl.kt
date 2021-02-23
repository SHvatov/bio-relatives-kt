package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.assembler.GenomeAssemblerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Component("GenomeAssemblerFactory")
class GenomeAssemblerFactoryImpl : GenomeAssemblerFactory {
    override fun create(
        assemblyCtx: AssemblyCtx,
        parentScope: CoroutineScope?
    ): GenomeAssembler = GenomeAssemblerImpl(assemblyCtx, parentScope)
}