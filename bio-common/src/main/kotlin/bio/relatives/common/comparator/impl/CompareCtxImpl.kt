package bio.relatives.common.comparator.impl

import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparatorAlgorithm

/**
 * @author shvatov
 */
data class CompareCtxImpl(
    override val algorithm: GenomeComparatorAlgorithm
) : CompareCtx