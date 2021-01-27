package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.RegionAssemblerFactory
import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.parser.FeatureParser
import bio.relatives.common.parser.RegionParserFactory
import java.nio.file.Path

/**
 * @author shvatov
 */
data class AssemblyCtxImpl(
    override val featureFilePath: Path,
    override val bamFilePaths: Map<Role, Path>,
    override val regionParserFactory: RegionParserFactory,
    override val regionAssemblerFactory: RegionAssemblerFactory,
    override val featureParser: FeatureParser
) : AssemblyCtx