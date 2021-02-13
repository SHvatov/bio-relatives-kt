package bio.relatives.common.cli.runner.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import bio.relatives.common.assembler.GenomeAssemblerFactory
import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.comparator.GenomeComparatorFactory
import bio.relatives.common.model.RoleAware
import bio.relatives.common.processor.CoroutineScopeAware.DefaultExceptionHandlerProvider.createLoggingExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.util.concurrent.Executors

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class RunnerImpl @Autowired constructor(
    private val assemblerFactory: GenomeAssemblerFactory,
    private val comparatorFactory: GenomeComparatorFactory,
    private val analyserFactory: ComparisonResultsAnalyserFactory
) : Runner {

    override val scope: CoroutineScope = CoroutineScope(
        CoroutineName("Runner") +
            createLoggingExceptionHandler(LOG) +
            Executors.newFixedThreadPool(EXECUTION_THREADS_NUMBER).asCoroutineDispatcher()
    )

    override fun run(context: RunnerCtx) = runBlocking {
        val assembler = assemblerFactory.create(
            context.pathToFeatureFile,
            getRoleMap(context),
            scope
        )

        assembler.use { asm ->
            val assemblyChannel = asm.assemble()
            val comparator = comparatorFactory.create(assemblyChannel, scope)

            comparator.use { cmp ->
                val comparisonChannel = cmp.compare()
                val analyzer = analyserFactory.create(scope)

                analyzer.use {
                    comparisonChannel.consumeEach { result ->
                        it.store(result)
                    }

                    println(it.analyse())
                }
            }
        }
    }

    private fun getRoleMap(context: RunnerCtx): Map<RoleAware.Role, Path> = hashMapOf(
        Pair(RoleAware.Role.SON, context.pathToSonFile),
        Pair(RoleAware.Role.FATHER, context.pathToFatherFile),
        Pair(RoleAware.Role.MOTHER, context.pathToMotherFile)
    )

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(RunnerImpl::class.java)

        const val EXECUTION_THREADS_NUMBER = 10
    }
}