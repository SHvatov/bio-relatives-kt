package bio.relatives.bio.y_str.model

import bio.relatives.common.model.Feature
import bio.relatives.common.model.RepeatMotifAware
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 02.02.2021
 */
data class MarkerFeature(
    override val gene: String,
    override val chromosome: String,
    override val start: Int,
    override val end: Int,
    override val repeatMotif: Pattern
) : Feature, RepeatMotifAware
