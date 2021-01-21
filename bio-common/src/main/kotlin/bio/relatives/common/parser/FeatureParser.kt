package bio.relatives.common.parser

import bio.relatives.common.model.Feature

/**
 * @author shvatov
 */
interface FeatureParser {
    fun parseFeatures(): List<Feature>
}