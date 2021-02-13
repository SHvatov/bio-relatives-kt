package bio.relatives.common.processor

import kotlinx.coroutines.CoroutineScope

/**
 * Note: This interface and [CoroutineScopeAware] one are used to implement
 * structured concurrency using standard [CoroutineScope]. Each `processor`
 * is aware of its scope and must implement [CoroutineScopeAware]. Also
 * it is aware of the coroutine scope of the parent coroutine if such exists
 * by implementing [CoroutineParentScopeAware]. It is assumed, that every
 * instance of [CoroutineParentScopeAware] is running inside of other coroutine.
 * @author shvatov
 */
interface CoroutineParentScopeAware {
    /**
     * Coroutine scope of the parent coroutine.
     */
    val parentScope: CoroutineScope
}