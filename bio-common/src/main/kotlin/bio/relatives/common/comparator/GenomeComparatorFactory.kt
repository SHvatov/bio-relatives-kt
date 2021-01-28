package bio.relatives.common.comparator

import bio.relatives.common.assembler.RegionBatch
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeComparatorFactory {
    fun create(inputChannel: ReceiveChannel<RegionBatch>): GenomeComparator
}