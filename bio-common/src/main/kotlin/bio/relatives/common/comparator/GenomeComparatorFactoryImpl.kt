package bio.relatives.common.comparator

import bio.relatives.common.assembler.RegionBatch
import bio.relatives.common.comparator.impl.GenomeComparatorImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Component("GenomeComparatorFactory")
class GenomeComparatorFactoryImpl @Autowired constructor(
    private val compareCtxFactory: CompareCtxFactory
) : GenomeComparatorFactory {
    override fun create(inputChannel: ReceiveChannel<RegionBatch>): GenomeComparator =
        GenomeComparatorImpl(compareCtxFactory.create(), inputChannel)
}
