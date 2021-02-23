package bio.relatives.common.cli.runner

/**
 * @author Created by Vladislav Marchenko on 05.02.2021
 */
interface Runner {
    /**
     * Runes our application logic with provided [ctx].
     */
    fun run(ctx: RunnerCtx)
}