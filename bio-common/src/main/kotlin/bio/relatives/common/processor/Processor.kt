package bio.relatives.common.processor

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

/**
 * @author shvatov
 */
interface Processor<P : Any, R : Any> :
    AutoCloseable, CoroutineScopeAware, CoroutineParentScopeAware {
    /**
     * Channel, which acts as input for the data to be processed
     * by the [Processor] instance. Is being handled by a separate
     * coroutine, which checks if some data has appeared here.
     */
    val inputChannel: SendChannel<P>

    /**
     * Channel, which acts as output of the data, which has been processed
     * by the [Processor] instance. The processing will be suspended until
     * all the data is fetched from the channel.
     */
    val outputChannel: ReceiveChannel<R>
}