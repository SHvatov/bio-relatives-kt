package bio.relatives.common.cli.parser

import bio.relatives.common.cli.runner.Runner
import kotlinx.cli.ArgParser

/**
 * @author Created by Vladislav Marchenko on 04.02.2021
 */
interface ArgParserFactory {
    /**
     * Creates [ArgParser]
     */
    fun create(runner: Runner): ArgParser
}