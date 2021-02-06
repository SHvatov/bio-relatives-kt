package bio.relatives.common.cli.command

import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.model.ComparatorType
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import org.springframework.stereotype.Component
import java.nio.file.Paths

/**
 * @author Created by Vladislav Marchenko on 04.02.2021
 */
@ExperimentalCli
@Component
class ComparisonCommand : Subcommand("-compare", "Comparison of three genomes") {

    lateinit var runner: Runner

    val comparatorType by option(
            ArgType.Choice<ComparatorType>(),
            shortName = "-ct",
            description = "Type of comparator"
    ).default(ComparatorType.NAIVE)

    val pathToFeatureFile by option(
            ArgType.String,
            shortName = "-ff",
            description = "Path to feature file"
    )

    val pathToFatherFile by option(
            ArgType.String,
            shortName = "-fgf",
            description = "Path to father genome file"
    )

    val pathToMotherFile by option(
            ArgType.String,
            shortName = "-mgf",
            description = "Path to mother genome file"
    )

    val pathToSonFile by option(
            ArgType.String,
            shortName = "-sgf",
            description = "Path to son genome file"
    )

    override fun execute() {
        runner.run(
                comparatorType,
                Paths.get(pathToFeatureFile!!),
                Paths.get(pathToFatherFile!!),
                Paths.get(pathToMotherFile!!),
                Paths.get(pathToSonFile!!)
        )
    }
}