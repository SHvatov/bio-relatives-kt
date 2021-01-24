package bio.relatives.common.model

import java.util.UUID

/**
 * @author shvatov
 */
interface IdentifierAware {
    /**
     * Unique identifier, which determines a class of related objects.
     * For example, all features related to one person and so on.
     */
    val identifier: UUID
}