package bio.relatives.bio.distance.model

import bio.relatives.common.model.Feature
import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 01.02.2021
 */
class BEDFeature(
        override val chromosome: String,
        override val gene: String,
        override val start: Int,
        override val end: Int,
        override val repeatMotif: Pattern
) : Feature