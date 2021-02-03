package bio.relatives.common.model

/**
 * Defines the roles, between which the comparison has happened.
 */
sealed class ComparisonParticipants(
    val firstRole: RoleAware.Role,
    val secondRole: RoleAware.Role
) {
    /**
     * Comparison between mother and son.
     */
    object MotherAndSon : ComparisonParticipants(RoleAware.Role.MOTHER, RoleAware.Role.SON)

    /**
     * Comparison between father and son.
     */
    object FatherAndSon : ComparisonParticipants(RoleAware.Role.FATHER, RoleAware.Role.SON)

    companion object {
        val COMPARISON_PAIRS = listOf(MotherAndSon, FatherAndSon)
    }
}