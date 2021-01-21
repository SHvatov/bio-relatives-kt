package bio.relatives.common.model

import bio.relatives.common.utils.validateChrom
import bio.relatives.common.utils.validateChromPosition
import bio.relatives.common.utils.validateNucleotideSequence
import java.util.UUID

/**
 * @author shvatov
 */
data class Region(
    override val personIdentifier: UUID = UUID.randomUUID(),
    override val sequence: String,
    override val chromosome: String,
    override val gene: String,
    override val start: Long,
    override val end: Long
) : NucleotideSequenceAware, ChromosomeAware, ChromosomePositionAware, PersonAware {
    init {
        validateChrom(this)
        validateChromPosition(this)
        validateNucleotideSequence(this)
    }
}