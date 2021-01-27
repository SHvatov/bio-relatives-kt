package bio.relatives.common.comparator.impl

import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.CompareCtxFactory
import bio.relatives.common.comparator.GenomeComparatorAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@Component("CompareCtxFactory")
class CompareCtxFactoryImpl @Autowired constructor(
    private val algorithm: GenomeComparatorAlgorithm
) : CompareCtxFactory {
    override fun create(): CompareCtx = CompareCtxImpl(algorithm)
}