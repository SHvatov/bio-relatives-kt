package bio.relatives.common.cli.runner

/**
 * @author Created by Vladislav Marchenko on 05.02.2021
 */
interface Runner : AutoCloseable {
    /**
     * TODO("add description")
     */
    fun run(context: RunnerCtx)
}