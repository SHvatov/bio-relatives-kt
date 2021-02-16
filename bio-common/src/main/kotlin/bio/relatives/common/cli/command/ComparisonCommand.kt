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
 * Implementation of comparison command of the CLI
 * @author Created by Vladislav Marchenko on 04.02.2021
 */
@ExperimentalCli
@Component
class ComparisonCommand @Autowired constructor(
        private val runner: Runner
) : Subcommand(COMPARISON_COMMAND_NAME, COMPARISON_COMMAND_DESCRIPTION) {

    private val comparatorType by option(
            ArgType.Choice<ComparatorType>(),
            shortName = COMPARATOR_TYPE_OPTION_SHORT_NAME,
            description = COMPARATOR_TYPE_OPTION_DESCRIPTION
    ).default(ComparatorType.NAIVE)

    private val pathToFeatureFile by option(
            ArgType.String,
            shortName = PATH_TO_FEATURE_FILE_OPTION_SHORT_NAME,
            description = PATH_TO_FEATURE_FILE_OPTION_DESCRIPTION
    )

    private val pathToFatherFile by option(
            ArgType.String,
            shortName = PATH_TO_FATHER_FILE_OPTION_SHORT_NAME,
            description = PATH_TO_FATHER_FILE_OPTION_DESCRIPTION
    )

    private val pathToMotherFile by option(
            ArgType.String,
            shortName = PATH_TO_MOTHER_FILE_OPTION_SHORT_NAME,
            description = PATH_TO_MOTHER_FILE_OPTION_DESCRIPTION
    )

    private val pathToSonFile by option(
            ArgType.String,
            shortName = PATH_TO_SON_FILE_OPTION_SHORT_NAME,
            description = PATH_TO_SON_FILE_OPTION_DESCRIPTION
    )

    override fun execute() {
        runner.run(
                RunnerCtx(
                        comparatorType,
                        Paths.get(pathToFeatureFile!!),
                        Paths.get(pathToFatherFile!!),
                        Paths.get(pathToMotherFile!!),
                        Paths.get(pathToSonFile!!)
                )
        )
    }

    private companion object {
        const val COMPARISON_COMMAND_NAME = "-compare"

        const val COMPARISON_COMMAND_DESCRIPTION = "Comparison of three genomes"

        const val COMPARATOR_TYPE_OPTION_SHORT_NAME = "ct"
        const val COMPARATOR_TYPE_OPTION_DESCRIPTION = "Type of comparator"

        const val PATH_TO_FEATURE_FILE_OPTION_SHORT_NAME = "ff"
        const val PATH_TO_FEATURE_FILE_OPTION_DESCRIPTION = "Path to feature file"

        const val PATH_TO_FATHER_FILE_OPTION_SHORT_NAME = "fgf"
        const val PATH_TO_FATHER_FILE_OPTION_DESCRIPTION = "Path to father genome file"

        const val PATH_TO_MOTHER_FILE_OPTION_SHORT_NAME = "mgf"
        const val PATH_TO_MOTHER_FILE_OPTION_DESCRIPTION = "Path to mother genome file"

        const val PATH_TO_SON_FILE_OPTION_SHORT_NAME = "sgf"
        const val PATH_TO_SON_FILE_OPTION_DESCRIPTION = "Path to son genome file"
    }
}