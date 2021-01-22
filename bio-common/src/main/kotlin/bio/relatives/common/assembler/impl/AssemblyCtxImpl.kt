package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.RegionAssemblerFactory
import bio.relatives.common.parser.RegionParserFactory
import java.nio.file.Path
import java.util.UUID

/**
 * @author shvatov
 */
data class AssemblyCtxImpl(
    override val featureFilePath: Path,
    override val bamFilePaths: Map<UUID, Path>,
    override val regionParserFactory: RegionParserFactory,
    override val regionAssemblerFactory: RegionAssemblerFactory
) : AssemblyCtx