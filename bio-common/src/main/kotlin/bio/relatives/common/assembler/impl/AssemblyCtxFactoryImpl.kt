package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.AssemblyCtxFactory
import bio.relatives.common.assembler.RegionAssemblerFactory
import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.parser.AbstractFeatureParser
import bio.relatives.common.parser.RegionParserFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author shvatov
 */
@Component
class AssemblyCtxFactoryImpl @Autowired constructor(
        private val regionAssemblerFactory: RegionAssemblerFactory,
        private val regionParserFactory: RegionParserFactory,
        private val featureParser: AbstractFeatureParser
) : AssemblyCtxFactory {
    override fun create(featureFilePath: Path, bamFilePaths: Map<Role, Path>): AssemblyCtx =
        AssemblyCtxImpl(
            featureFilePath,
            bamFilePaths,
            regionParserFactory,
            regionAssemblerFactory,
            featureParser
        )
}