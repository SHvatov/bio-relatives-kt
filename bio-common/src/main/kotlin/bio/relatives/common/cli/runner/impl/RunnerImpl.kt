package bio.relatives.common.cli.runner.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import bio.relatives.common.assembler.GenomeAssemblerFactory
import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.cli.runner.RunnerCtx
import bio.relatives.common.comparator.GenomeComparatorFactory
import bio.relatives.common.model.RoleAware
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path

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
    override fun run(context: RunnerCtx) = runBlocking {
        val assembler = assemblerFactory.create(
            context.pathToFeatureFile,
            getRoleMap(context)
        )

        assembler.use { asm ->
            val assemblyChannel = asm.assemble()
            val comparator = comparatorFactory.create(assemblyChannel)

            comparator.use { cmp ->
                val comparisonChannel = cmp.compare()
                val analyzer = analyserFactory.create()

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
}