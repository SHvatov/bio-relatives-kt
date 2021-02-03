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
    override val qualities: Array<Double>,
    override val gene: String,
    override val chromosome: String,
    override val start: Int,
    override val end: Int
) : NucleotideSequenceAware, ChromosomeAware, ChromosomePositionAware, RoleAware {
    init {
        validateChrom(this)
        validateChromPosition(this)
        validateNucleotideSequence(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Region

        if (role != other.role) return false
        if (sequence != other.sequence) return false
        if (!qualities.contentEquals(other.qualities)) return false
        if (chromosome != other.chromosome) return false
        if (gene != other.gene) return false
        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = role.hashCode()
        result = 31 * result + sequence.hashCode()
        result = 31 * result + qualities.contentHashCode()
        result = 31 * result + chromosome.hashCode()
        result = 31 * result + gene.hashCode()
        result = 31 * result + start
        result = 31 * result + end
        return result
    }
}