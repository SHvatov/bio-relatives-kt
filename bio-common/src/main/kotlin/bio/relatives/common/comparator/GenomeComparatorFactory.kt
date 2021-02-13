package bio.relatives.common.comparator

import bio.relatives.common.model.RegionBatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeComparatorFactory {
    fun create(
        inputChannel: ReceiveChannel<RegionBatch>,
        parentScope: CoroutineScope
    ): GenomeComparator
}