package bio.relatives.common.comparator

import bio.relatives.common.model.ComparisonResult
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface GenomeComparator : AutoCloseable {
    /**
     * Compares the assembled genome regions from the channel provided on initialization.
     * Starts a separate coroutine, which is responsible for the comparison of the genomes.
     * Returns a [ReceiveChannel], which contains [ComparisonResult], which represent
     * the result of comparison of one region between all the members.
     */
    fun compare(): ReceiveChannel<ComparisonResult>
}