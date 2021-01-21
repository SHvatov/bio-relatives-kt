package bio.relatives.common.processor

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import java.util.UUID

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AssemblyProcessor(
    private val assemblyCtx: AssemblyCtx,
    inputChannel: ReceiveChannel<Feature>
) : AbstractProcessor<Feature, RegionBatch>(inputChannel) {
    override fun makeSubProcessor() = object : SubProcessor() {
        /**
         * List of identifiers of the people, whose genomes we are working with.
         */
        private val personIdentifiers = with(assemblyCtx) {
            bamFilePaths.map { it.key }
        }

        /**
         * Map of [RegionParser] instances grouped by identifier of the person
         * they are associated with.
         */
        private val regionParsersByPerson = with(assemblyCtx) {
            bamFilePaths
                .map { (id, path) -> regionParserFactory.create(id, path) }
                .associateBy { it.personIdentifier }
        }

        /**
         * Map of [RegionAssembler] instances grouped by identifier of the person
         * they are associated with.
         */
        private val regionAssemblersByPerson = with(assemblyCtx) {
            bamFilePaths
                .map { (id, path) -> regionAssemblerFactory.create(id, path) }
                .associateBy { it.personIdentifier }
        }

        override suspend fun process(payload: Feature): RegionBatch {
            return coroutineScope {
                val assemblyResults = personIdentifiers.map {
                    async {
                        assemble(it, payload)
                    }
                }

                assemblyResults
                    .map { it.await() }
                    .associateBy { it.personIdentifier }
                    .toMap(RegionBatch())
            }
        }

        /**
         * Synchronously assembles one region of the genome of the person with [personIdentifier]
         * using the provided [feature].
         */
        private fun assemble(personIdentifier: UUID, feature: Feature): Region {
            val records = regionParsersByPerson
                .getValue(personIdentifier)
                .parseRegion(feature)

            return regionAssemblersByPerson
                .getValue(personIdentifier)
                .assemble(feature, records)
        }
    }
}