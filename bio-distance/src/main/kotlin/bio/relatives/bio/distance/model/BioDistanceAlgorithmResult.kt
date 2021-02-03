package bio.relatives.bio.distance.model

import bio.relatives.common.model.ComparisonResult

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioDistanceAlgorithmResult(
        override val result: Pair<Int, Int>
) : ComparisonResult.AlgorithmResult