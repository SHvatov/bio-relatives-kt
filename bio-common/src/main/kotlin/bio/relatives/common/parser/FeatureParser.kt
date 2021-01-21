package bio.relatives.common.parser

import bio.relatives.common.model.Feature
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author shvatov
 */
interface FeatureParser : AutoCloseable {
    fun parseFeatures(): ReceiveChannel<Feature>
}