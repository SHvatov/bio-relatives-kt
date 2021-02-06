package bio.relatives.common.cli.parser.impl

import bio.relatives.common.cli.command.ComparisonCommand
import bio.relatives.common.cli.parser.ArgParserFactory
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import org.springframework.stereotype.Component

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
class ArgParserFactoryImpl : ArgParserFactory {
    @ExperimentalCli
    override fun create(): ArgParser {
        val argParser = ArgParser("bio-relatives")
        argParser.subcommands(ComparisonCommand())
        return argParser
    }
}