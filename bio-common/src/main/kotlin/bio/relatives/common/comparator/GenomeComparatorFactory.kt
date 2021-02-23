package bio.relatives.common.comparator

import bio.relatives.common.assembler.GenomeAssemblyResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeComparatorFactory {
    /**
     * Creates an instance of [GenomeComparator] based on the provided [compareCtx]
     * and [inputChannel].
     */
    fun create(
        compareCtx: CompareCtx,
        inputChannel: ReceiveChannel<GenomeAssemblyResult>,
        parentScope: CoroutineScope? = null
    ): GenomeComparator
}