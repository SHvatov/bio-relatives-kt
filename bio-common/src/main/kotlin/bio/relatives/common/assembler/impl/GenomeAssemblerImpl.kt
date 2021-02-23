package bio.relatives.common.assembler.impl

import bio.relatives.common.assembler.AssemblyCtx
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.assembler.GenomeAssemblyResult
import bio.relatives.common.assembler.GenomeAssemblyTask
import bio.relatives.common.assembler.RegionAssembler
import bio.relatives.common.model.Feature
import bio.relatives.common.model.RegionBatch
import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.parser.RegionParser
import com.shvatov.processor.CoroutineScopeAware
import com.shvatov.processor.config.TaskProcessorConfiguration
import com.shvatov.processor.impl.TaskProcessorImpl
import com.shvatov.processor.use
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import org.apache.commons.pool.BasePoolableObjectFactory
import org.apache.commons.pool.impl.GenericObjectPool
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.EnumMap
import java.util.concurrent.Executors
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class GenomeAssemblerImpl(
    /**
     * Assembling context, which contains singletons and some additional data,
     * required for the assembling. Will be used on lower levels of the assembling.
     */
    private val assembleCtx: AssemblyCtx,

    /**
     * Scope of the parent coroutine this assembler is called from.
     */
    override val parentScope: CoroutineScope?
) : GenomeAssembler {

    /**
     * Parent scope for the execution of all the assembling coroutines.
     */
    override val scope = CoroutineScope(
        (parentScope?.coroutineContext ?: EmptyCoroutineContext) +
            CoroutineName("Assembler") +
            Executors.newFixedThreadPool(DEFAULT_ASSEMBLY_THREADS).asCoroutineDispatcher() +
            CoroutineScopeAware.exceptionHandler(LOG)
    )

    /**
     * Processors, which will be used for the parallel execution
     * of genome assembly tasks.
     */
    private val processor = TaskProcessorImpl<Feature, RegionBatch>(
        PROCESSOR_CONFIGURATION,
        scope
    )

    /**
     * Pool of [AssemblyTools], that are used for the genome assembly in processors.
     */
    private val assemblyToolsPool = GenericObjectPool(
        AssemblyToolsFactory(), PROCESSOR_CONFIGURATION.subProcessorsNumber
    )

    override fun assemble(): ReceiveChannel<GenomeAssemblyResult> =
        processor.outputChannel.also {
            scope.launch {
                processor.use {
                    with(assembleCtx) {
                        val features = featureParser.parseFeatures(assembleCtx.featureFilePath)
                        for (feature in features) {
                            submit(
                                GenomeAssemblyTask(feature) {
                                    val tools = assemblyToolsPool.borrowObject()
                                    try {
                                        assembleGenomes(it, tools)
                                    } finally {
                                        assemblyToolsPool.returnObject(tools)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

    override fun close() {
        assemblyToolsPool.close()
    }

    /**
     * Performs synchronized assembly of the genomes from [feature] by provided [tools].
     */
    private fun assembleGenomes(feature: Feature, tools: AssemblyTools): RegionBatch {
        val assemblyResults = assembleCtx.bamFilePaths.keys.map { role ->
            val (parser, assembler) = tools.parserByRole.getValue(role) to tools.assembler
            val records = parser.parseRegion(feature)
            assembler.assemble(role, feature, records)
        }.associateByTo(EnumMap(Role::class.java)) { it.role }

        return RegionBatch(feature, assemblyResults)
    }

    /**
     * Utils data class, which is used to store parser and assembler that
     * are associated with one of the people we are working with.
     */
    private data class AssemblyTools(
        val parserByRole: Map<Role, RegionParser>,
        val assembler: RegionAssembler
    )

    /**
     * Factory, which is used to add [AssemblyTools] to the pool.
     */
    private inner class AssemblyToolsFactory : BasePoolableObjectFactory<AssemblyTools>() {
        override fun makeObject() = with(assembleCtx) {
            AssemblyTools(
                parserByRole = bamFilePaths.mapValues { regionParserFactory.create(it.value) },
                assembler = regionAssemblerFactory.create()
            )
        }

        override fun destroyObject(obj: AssemblyTools) {
            obj.parserByRole.forEach {
                it.value.close()
            }
            super.destroyObject(obj)
        }
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(GenomeAssemblerImpl::class.java)

        /**
         * Number of threads to be used by the assembler coroutine dispatcher.
         */
        const val DEFAULT_ASSEMBLY_THREADS = 3

        /**
         * Default [TaskProcessorImpl] configuration.
         * TODO: at some point make it editable by the user
         */
        val PROCESSOR_CONFIGURATION = TaskProcessorConfiguration(
            exceptionHandler = CoroutineScopeAware.exceptionHandler(LOG),
        )
    }
}