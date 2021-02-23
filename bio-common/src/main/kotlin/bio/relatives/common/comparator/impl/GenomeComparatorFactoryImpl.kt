package bio.relatives.common.comparator.impl

import bio.relatives.common.assembler.GenomeAssemblyResult
import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.comparator.GenomeComparatorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Component("GenomeComparatorFactory")
class GenomeComparatorFactoryImpl : GenomeComparatorFactory {
    override fun create(
        compareCtx: CompareCtx,
        inputChannel: ReceiveChannel<GenomeAssemblyResult>,
        parentScope: CoroutineScope?
    ): GenomeComparator = GenomeComparatorImpl(compareCtx, inputChannel, parentScope)
}
