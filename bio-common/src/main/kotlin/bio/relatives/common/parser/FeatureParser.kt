package bio.relatives.common.parser

import bio.relatives.common.model.Feature

/**
 * @author shvatov
 */
interface FeatureParser : AutoCloseable {
    fun parseFeatures(): List<Feature>
}