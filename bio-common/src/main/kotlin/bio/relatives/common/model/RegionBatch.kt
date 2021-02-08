package bio.relatives.common.model


/**
 * @author Created by Vladislav Marchenko on 03.02.2021
 */
data class RegionBatch(
    val feature: Feature,
    val regionsByRole: Map<RoleAware.Role, Region>
)
