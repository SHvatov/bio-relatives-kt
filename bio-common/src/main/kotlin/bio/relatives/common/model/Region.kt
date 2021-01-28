package bio.relatives.common.model

import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.utils.validateChrom
import bio.relatives.common.utils.validateChromPosition
import bio.relatives.common.utils.validateNucleotideSequence

/**
 * @author shvatov
 */
data class Region(
    override val role: Role,
    override val sequence: String,
    override val chromosome: String,
    override val gene: String,
    override val start: Int,
    override val end: Int
) : NucleotideSequenceAware, ChromosomeAware, ChromosomePositionAware, RoleAware {
    init {
        validateChrom(this)
        validateChromPosition(this)
        validateNucleotideSequence(this)
    }
}