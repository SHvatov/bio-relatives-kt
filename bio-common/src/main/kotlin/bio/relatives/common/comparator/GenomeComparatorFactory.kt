package bio.relatives.common.comparator

import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.model.RegionBatch
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeComparatorFactory {
    fun create(inputChannel: ReceiveChannel<RegionBatch>): GenomeComparator
}