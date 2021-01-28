package bio.relatives.common.model

/**
 * @author shvatov
 */
interface RoleAware {
    /**
     * Determines the role of the person in the inheritance tree
     * this abstract object is related to.
     */
    val role: Role

    /**
     * Suggested role of the person in the inheritance tree.
     */
    enum class Role {
        FATHER,
        MOTHER,
        SON
    }
}