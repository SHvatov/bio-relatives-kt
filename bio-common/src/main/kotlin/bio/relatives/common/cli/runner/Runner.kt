package bio.relatives.common.cli.runner

import bio.relatives.common.cli.runner.impl.RunnerCtx
import bio.relatives.common.processor.CoroutineScopeAware

/**
 * @author Created by Vladislav Marchenko on 05.02.2021
 */
interface Runner : CoroutineScopeAware {
    /**
     * Runes our application logic with [context]
     */
    fun run(context: RunnerCtx)
}