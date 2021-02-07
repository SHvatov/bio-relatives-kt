package bio.relatives.common.cli.runner.impl

import bio.relatives.common.analyzer.ComparisonResultsAnalyserFactory
import bio.relatives.common.assembler.GenomeAssemblerFactory
import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.cli.runner.RunnerCtx
import bio.relatives.common.comparator.GenomeComparatorFactory
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.RoleAware
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
class RunnerImpl @Autowired constructor(
        private val genomeAssemblerFactory: GenomeAssemblerFactory,
        private val genomeComparatorFactory: GenomeComparatorFactory,
        private val comparisonResultsAnalyserFactory: ComparisonResultsAnalyserFactory
) : Runner {

    private val scope = CoroutineScope(
            SupervisorJob() + CoroutineName("Iterator via comparator")
    )

    private lateinit var genomeComparatorChannel: ReceiveChannel<ComparisonResult>

    override fun run(context: RunnerCtx) {
        val genomeAssembler = genomeAssemblerFactory.create(
                context.pathToFeatureFile,
                getRoleMap(context)
        )

        val genomeComparator = genomeComparatorFactory.create(genomeAssembler.assemble())

        val genomeAnalyser = comparisonResultsAnalyserFactory.create()

        genomeComparatorChannel = genomeComparator.compare()

        val genomeComparatorChannelIterator = genomeComparatorChannel.iterator()

        scope.launch {
            while (genomeComparatorChannelIterator.hasNext()) {
                genomeAnalyser.store(genomeComparatorChannelIterator.next())
            }
        }

        println(genomeAnalyser.analyse())
    }

    override fun close() {
        scope.cancel()

        genomeComparatorChannel.cancel()
    }

    private fun getRoleMap(context: RunnerCtx): Map<RoleAware.Role, Path> = hashMapOf(
            Pair(RoleAware.Role.SON, context.pathToSonFile),
            Pair(RoleAware.Role.FATHER, context.pathToFatherFile),
            Pair(RoleAware.Role.MOTHER, context.pathToMotherFile)
    )
}