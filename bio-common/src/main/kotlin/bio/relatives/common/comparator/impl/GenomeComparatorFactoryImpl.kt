package bio.relatives.common.comparator.impl

import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparator
import bio.relatives.common.comparator.GenomeComparatorFactory
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@Component("GenomeComparatorFactory")
class GenomeComparatorFactoryImpl : GenomeComparatorFactory {
    override fun create(compareCtx: CompareCtx): GenomeComparator = GenomeComparatorImpl(compareCtx)
}
