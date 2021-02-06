package bio.relatives.common.cli.command

import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.cli.runner.RunnerCtx
import bio.relatives.common.model.ComparatorType
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Paths

/**
 * @author Created by Vladislav Marchenko on 04.02.2021
 */
/**
 * TODO("add description")
 */
@ExperimentalCli
@Component
class ComparisonCommand @Autowired constructor(
        private val runner: Runner
) : Subcommand("-compare", "Comparison of three genomes") {

    private val comparatorType by option(
            ArgType.Choice<ComparatorType>(),
            shortName = "ct",
            description = "Type of comparator"
    ).default(ComparatorType.NAIVE)

    private val pathToFeatureFile by option(
            ArgType.String,
            shortName = "ff",
            description = "Path to feature file"
    )

    private val pathToFatherFile by option(
            ArgType.String,
            shortName = "fgf",
            description = "Path to father genome file"
    )

    private val pathToMotherFile by option(
            ArgType.String,
            shortName = "mgf",
            description = "Path to mother genome file"
    )

    private val pathToSonFile by option(
            ArgType.String,
            shortName = "sgf",
            description = "Path to son genome file"
    )

    override fun execute() {
        runner.run(
                RunnerCtx(comparatorType,
                        Paths.get(pathToFeatureFile!!),
                        Paths.get(pathToFatherFile!!),
                        Paths.get(pathToMotherFile!!),
                        Paths.get(pathToSonFile!!))
        )
    }
}