package bio.relatives.common.model

import bio.relatives.common.utils.validateChrom
import bio.relatives.common.utils.validateChromPosition
import bio.relatives.common.utils.validateNucleotideSequence

/**
 * @author shvatov
 */
data class Region(
    override val sequence: String,
    override val chromosome: String,
    override val gene: String,
    override val start: Long,
    override val end: Long
) : NucleotideSequenceAware, ChromosomeAware, ChromosomePositionAware {
    init {
        validateChrom(this)
        validateChromPosition(this)
        validateNucleotideSequence(this)
    }
}