package bio.relatives.common.processor

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.parser.RegionParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import java.util.UUID

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AssemblyProcessor(
    private val assemblyCtx: AssemblyCtx
) : AbstractProcessor<Feature, RegionBatch>() {
    /**
     * Utils data class, which is used to store parser and assembler that
     * are associated with one of the people we are workng with.
     */
    private data class AssemblyTool(
        val parser: RegionParser,
        val assembler: RegionAssembler
    )

    override fun makeSubProcessor() = object : AbstractSubProcessor() {
        /**
         * List of identifiers of the people, whose genomes we are working with.
         */
        private val identifiers = assemblyCtx.bamFilePaths.keys

        /**
         * Map of the tools grouped by the id of the person the are related to.
         */
        private val assemblyToolsById = with(assemblyCtx) {
            bamFilePaths.mapValues { (_, path) ->
                AssemblyTool(
                    parser = regionParserFactory.create(path),
                    assembler = regionAssemblerFactory.create()
                )
            }
        }

        override suspend fun process(parentScope: CoroutineScope, payload: Feature): RegionBatch {
            val assemblyResults = identifiers.map {
                parentScope.async {
                    assemble(it, payload)
                }
            }

            return assemblyResults
                .map { it.await() }
                .associateBy { it.identifier }
                .toMap(RegionBatch())
        }

        override fun close() {
            assemblyToolsById.values.forEach { it.parser.close() }
            super.close()
        }

        /**
         * Synchronously assembles one region of the genome of the person with [identifier]
         * using the provided [feature].
         */
        private fun assemble(identifier: UUID, feature: Feature): Region {
            val (parser, assembler) = assemblyToolsById[identifier]!!
            val records = parser.parseRegion(feature)
            return assembler.assemble(feature, records)
        }
    }
}