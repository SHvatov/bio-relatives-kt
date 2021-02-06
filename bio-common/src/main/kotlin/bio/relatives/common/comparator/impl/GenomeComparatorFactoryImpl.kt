package bio.relatives.common.comparator.impl

import bio.relatives.common.comparator.CompareCtxFactory
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.comparator.GenomeComparatorFactory
import bio.relatives.common.model.RegionBatch
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
