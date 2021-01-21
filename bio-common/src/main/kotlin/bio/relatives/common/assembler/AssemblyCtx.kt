package bio.relatives.common.assembler

import bio.relatives.common.parser.RegionParserFactory
import java.nio.file.Path
import java.util.UUID

/**
 * @author shvatov
 */
interface AssemblyCtx {
    /**
     * Path to the feature file.
     */
    val featureFilePath: Path

    /**
     * Set of paths to the BAM files used as reference data for
     * the genome assembly.
     */
    val bamFilePaths: Map<UUID, Path>

    /**
     * Factory, which is used to create [RegionParser] instances.
     */
    val regionParserFactory: RegionParserFactory

    /**
     * Factory, which is used to create [RegionAssembler] instances.
     */
    val regionAssemblerFactory: RegionAssemblerFactory
}