package bio.relatives.common.cli.parser

import kotlinx.cli.ArgParser

/**
 * @author Created by Vladislav Marchenko on 04.02.2021
 */
interface ArgParserFactory {
    /**
     * TODO("add description")
     */
    fun create(): ArgParser
}