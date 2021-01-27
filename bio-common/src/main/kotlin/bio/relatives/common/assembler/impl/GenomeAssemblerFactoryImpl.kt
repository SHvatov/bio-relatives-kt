package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtxFactory
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.assembler.GenomeAssemblerFactory
import bio.relatives.common.parser.FeatureParser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Component("GenomeAssemblerFactory")
class GenomeAssemblerFactoryImpl @Autowired constructor(
    private val assemblyCtxFactory: AssemblyCtxFactory,
    private val featureParser: FeatureParser
) : GenomeAssemblerFactory {
    override fun create(featureFilePath: Path, vararg bamFilePaths: Path): GenomeAssembler =
        GenomeAssemblerImpl(
            assemblyCtxFactory.create(featureFilePath, *bamFilePaths),
            featureParser
        )
}