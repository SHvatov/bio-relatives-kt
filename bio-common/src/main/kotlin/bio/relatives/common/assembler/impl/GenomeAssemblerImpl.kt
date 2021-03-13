package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.RegionBatch
import bio.relatives.common.model.RoleAware.Role
import java.util.EnumMap

/**
 * @author shvatov
 */
class GenomeAssemblerImpl(
    /**
     * Assembling context, which contains singletons and some additional data,
     * required for the assembling. Will be used on lower levels of the assembling.
     */
    private val assembleCtx: AssemblyCtx
) : GenomeAssembler {
    private val assembler = assembleCtx.regionAssemblerFactory.create()

    private val parserByRoles = assembleCtx.bamFilePaths.mapValues {
        assembleCtx.regionParserFactory.create(it.value)
    }

    override fun assemble(): List<RegionBatch> {
        return with(assembleCtx) {
            val features = featureParser.parseFeatures(assembleCtx.featureFilePath)
            features.map {
                assembleGenomes(it)
            }
        }
    }

    /**
     * Performs synchronized assembly of the genomes from [feature].
     */
    private fun assembleGenomes(feature: Feature): RegionBatch {
        val assemblyResults = parserByRoles.map { (role, parser) ->
            val records = parser.parseRegion(feature)
            assembler.assemble(role, feature, records)
        }.associateByTo(EnumMap(Role::class.java)) { it.role }
        return RegionBatch(feature, assemblyResults)
    }
}