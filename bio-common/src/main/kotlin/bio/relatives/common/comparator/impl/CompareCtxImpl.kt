package bio.relatives.common.comparator.impl

import bio.relatives.common.comparator.CompareCtx
import bio.relatives.common.comparator.GenomeComparisonMethod

/**
 * @author shvatov
 */
data class CompareCtxImpl(
    override val comparisonMethod: GenomeComparisonMethod
) : CompareCtx