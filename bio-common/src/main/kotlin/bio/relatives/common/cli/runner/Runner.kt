package bio.relatives.common.cli.runner

/**
 * @author Created by Vladislav Marchenko on 05.02.2021
 */
interface Runner : AutoCloseable {
    /**
     * Runes our application logic with [context]
     */
    fun run(context: RunnerCtx)
}