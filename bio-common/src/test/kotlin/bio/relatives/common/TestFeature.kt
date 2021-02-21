package bio.relatives.common

import bio.relatives.common.model.Feature

/**
 * Private impl of the [Feature] interface, used for tests.
 */
internal data class FeatureTestImpl(
    override val gene: String,
    override val chromosome: String,
    override val start: Int,
    override val end: Int
) : Feature