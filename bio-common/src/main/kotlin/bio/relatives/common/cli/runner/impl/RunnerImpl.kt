package bio.relatives.common.cli.runner.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import bio.relatives.common.assembler.AssemblyCtxFactory
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.assembler.GenomeAssemblerFactory
import bio.relatives.common.assembler.GenomeAssemblyResult
import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.cli.runner.RunnerCtx
import bio.relatives.common.comparator.CompareCtxFactory
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.comparator.GenomeComparatorFactory
import bio.relatives.common.model.RoleAware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path
import kotlin.system.measureNanoTime

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class RunnerImpl @Autowired constructor(
    private val assemblyCtxFactory: AssemblyCtxFactory,
    private val assemblerFactory: GenomeAssemblerFactory,
    private val compareCtxFactory: CompareCtxFactory,
    private val comparatorFactory: GenomeComparatorFactory,
    private val analyserFactory: ComparisonResultsAnalyserFactory
) : Runner {
    override fun run(ctx: RunnerCtx): Unit = runBlocking {
        measureNanoTime {
            val assembler = prepareAssembler(ctx)

            assembler.use { asm ->
                val assemblyChannel = asm.assemble()
                val comparator = prepareComparator(assemblyChannel)

                val comparisonChannel = comparator.compare()
                val analyzer = analyserFactory.create(comparisonChannel, this)

                println(analyzer.analyse())
            }
        }.also {
            println("Time spent: $it")
        }
    }

    private fun CoroutineScope.prepareAssembler(ctx: RunnerCtx): GenomeAssembler {
        val assemblyCtx = assemblyCtxFactory.create(ctx.pathToFeatureFile, getRoleMap(ctx))
        return assemblerFactory.create(assemblyCtx, this)
    }

    private fun CoroutineScope.prepareComparator(
        assemblyChannel: ReceiveChannel<GenomeAssemblyResult>
    ): GenomeComparator {
        val compareCtx = compareCtxFactory.create()
        return comparatorFactory.create(compareCtx, assemblyChannel, this)
    }

    private fun getRoleMap(ctx: RunnerCtx): Map<RoleAware.Role, Path> = hashMapOf(
        Pair(RoleAware.Role.SON, ctx.pathToSonFile),
        Pair(RoleAware.Role.FATHER, ctx.pathToFatherFile),
        Pair(RoleAware.Role.MOTHER, ctx.pathToMotherFile)
    )
}