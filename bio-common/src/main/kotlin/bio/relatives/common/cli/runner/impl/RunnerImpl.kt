package bio.relatives.common.cli.runner.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import bio.relatives.common.assembler.AssemblyCtxFactory
import bio.relatives.common.assembler.GenomeAssembler
import bio.relatives.common.assembler.GenomeAssemblerFactory
import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.cli.runner.RunnerCtx
import bio.relatives.common.comparator.CompareCtxFactory
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.comparator.GenomeComparatorFactory
import bio.relatives.common.model.RoleAware
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path
import kotlin.system.measureNanoTime

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
class RunnerImpl @Autowired constructor(
    private val assemblyCtxFactory: AssemblyCtxFactory,
    private val assemblerFactory: GenomeAssemblerFactory,
    private val compareCtxFactory: CompareCtxFactory,
    private val comparatorFactory: GenomeComparatorFactory,
    private val analyserFactory: ComparisonResultsAnalyserFactory
) : Runner {

    override fun run(ctx: RunnerCtx) {
        val time = measureNanoTime {
            val assembler = prepareAssembler(ctx)
            val assemblyResults = assembler.assemble()
            val comparator = prepareComparator()

            val comparisonResults = comparator.compare(assemblyResults)
            val analyzer = analyserFactory.create()

            println(analyzer.analyse(comparisonResults))
        }
        println(time)
    }

    private fun prepareAssembler(ctx: RunnerCtx): GenomeAssembler {
        val assemblyCtx = assemblyCtxFactory.create(ctx.pathToFeatureFile, getRoleMap(ctx))
        return assemblerFactory.create(assemblyCtx)
    }

    private fun prepareComparator(): GenomeComparator {
        val compareCtx = compareCtxFactory.create()
        return comparatorFactory.create(compareCtx)
    }

    private fun getRoleMap(ctx: RunnerCtx): Map<RoleAware.Role, Path> = hashMapOf(
        Pair(RoleAware.Role.SON, ctx.pathToSonFile),
        Pair(RoleAware.Role.FATHER, ctx.pathToFatherFile),
        Pair(RoleAware.Role.MOTHER, ctx.pathToMotherFile)
    )
}