package bio.relatives.common.comparator

/**
 * @author shvatov
 */
interface GenomeComparatorFactory {
    fun create(compareCtx: CompareCtx): GenomeComparator
}