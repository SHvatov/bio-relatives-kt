package bio.relatives.common.processor

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RegionBatch
import bio.relatives.common.model.RoleAware
import bio.relatives.common.parser.RegionParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AssemblyProcessor(
    private val assemblyCtx: AssemblyCtx,
    parentScope: CoroutineScope
) : AbstractProcessor<Feature, RegionBatch>(parentScope) {
    override fun makeSubProcessor() = object : AbstractSubProcessor() {
        /**
         * List of the roles of the people, whose genomes we are working with.
         */
        private val roles = assemblyCtx.bamFilePaths.keys

        /**
         * Map of the tools grouped by the id of the person the are related to.
         */
        private val assemblyToolsById = with(assemblyCtx) {
            bamFilePaths.mapValues { (_, path) ->
                AssemblyTools(
                    parser = regionParserFactory.create(path),
                    assembler = regionAssemblerFactory.create()
                )
            }
        }

        override suspend fun process(parentScope: CoroutineScope, batch: Feature): RegionBatch {
            val assemblyResults = roles.map {
                parentScope.async {
                    assemble(it, batch)
                }
            }

            return RegionBatch(batch, assemblyResults
                    .map { it.await() }
                    .associateByTo(HashMap()) { it.role })
        }

        override fun close() {
            assemblyToolsById.values.forEach { it.parser.close() }
            super.close()
        }

        /**
         * Synchronously assembles one region of the genome of the person with [role]
         * using the provided [feature].
         */
        private fun assemble(role: RoleAware.Role, feature: Feature): Region {
            val (parser, assembler) = assemblyToolsById.getValue(role)
            val records = parser.parseRegion(feature)
            return assembler.assemble(role, feature, records)
        }
    }

    /**
     * Utils data class, which is used to store parser and assembler that
     * are associated with one of the people we are workng with.
     */
    private data class AssemblyTools(
        val parser: RegionParser,
        val assembler: RegionAssembler
    )
}
