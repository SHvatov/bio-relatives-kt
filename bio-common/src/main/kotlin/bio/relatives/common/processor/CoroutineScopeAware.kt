package bio.relatives.common.processor

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import org.slf4j.Logger

/**
 * @author shvatov
 */
interface CoroutineScopeAware {
    /**
     * [CoroutineScope] instance, which is used to launch the coroutines.
     */
    val scope: CoroutineScope

    companion object DefaultExceptionHandlerProvider {
        /**
         * Creates an instance of [CoroutineExceptionHandler] too be used in the
         * sub-coroutines for logging the exception.
         */
        fun createLoggingExceptionHandler(log: Logger): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { ctx, exception ->
                log.error(
                    "Exception occurred while processing data in ${ctx[CoroutineName.Key]}:",
                    exception
                )
            }
        }
    }
}