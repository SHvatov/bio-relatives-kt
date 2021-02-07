package bio.relatives.common.cli.parser.impl

import bio.relatives.common.cli.command.ComparisonCommand
import bio.relatives.common.cli.parser.ArgParserFactory
import bio.relatives.common.cli.runner.Runner
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import org.springframework.stereotype.Component

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
class ArgParserFactoryImpl : ArgParserFactory {
    @ExperimentalCli
    override fun create(runner: Runner): ArgParser {
        val argParser = ArgParser(PROGRAM_NAME)
        argParser.subcommands(ComparisonCommand(runner))
        return argParser
    }

    companion object {
        private const val PROGRAM_NAME = "bio-relatives"
    }
}