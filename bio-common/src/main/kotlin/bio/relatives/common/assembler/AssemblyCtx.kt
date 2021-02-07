package bio.relatives.common.assembler

import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.parser.AbstractFeatureParser
import bio.relatives.common.parser.RegionParserFactory
import java.nio.file.Path

/**
 * @author shvatov
 */
interface AssemblyCtx {
    /**
     * Path to the feature file.
     */
    val featureFilePath: Path

    /**
     * Set of paths to the BAM files used as reference data for the genome assembly
     * mapped by the unique id of the person whom they belong.
     */
    val bamFilePaths: Map<Role, Path>

    /**
     * Factory, which is used to create [RegionParser] instances.
     */
    val regionParserFactory: RegionParserFactory

    /**
     * Factory, which is used to create [RegionAssembler] instances.
     */
    val regionAssemblerFactory: RegionAssemblerFactory

    /**
     * [AbstractFeatureParser] extension, used to parse feature from corresponding file.
     */
    val featureParser: AbstractFeatureParser
}