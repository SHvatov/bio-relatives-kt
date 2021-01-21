package bio.relatives.common.model

import java.util.UUID

/**
 * @author shvatov
 */
interface PersonAware {
    /**
     * Unique identifier of the person this object is associated with.
     */
    val personIdentifier: UUID
}