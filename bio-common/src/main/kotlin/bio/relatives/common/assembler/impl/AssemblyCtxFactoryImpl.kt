package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.AssemblyCtxFactory
import bio.relatives.common.assembler.RegionAssemblerFactory
import bio.relatives.common.parser.RegionParserFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.util.UUID

/**
 * @author shvatov
 */
@Component
class AssemblyCtxFactoryImpl @Autowired constructor(
    private val regionAssemblerFactory: RegionAssemblerFactory,
    private val regionParserFactory: RegionParserFactory
) : AssemblyCtxFactory {
    override fun create(featureFilePath: Path, vararg bamFilePaths: Path): AssemblyCtx =
        AssemblyCtxImpl(
            featureFilePath,
            bamFilePaths.associateBy { UUID.randomUUID() },
            regionParserFactory,
            regionAssemblerFactory
        )
}