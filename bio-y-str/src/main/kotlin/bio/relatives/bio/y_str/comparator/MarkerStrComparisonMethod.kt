package bio.relatives.bio.y_str.comparator

import bio.relatives.bio.y_str.model.MarkerFeature
import bio.relatives.common.comparator.GenomeComparisonMethod
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.utils.calculateAverageQuality
import bio.relatives.common.utils.getNormalizedAlignments
import org.springframework.stereotype.Component
import java.lang.Integer.min
import java.lang.Math.max

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
@Component("MarkerStrComparisonMethod")
class MarkerStrComparisonMethod : GenomeComparisonMethod {

    override fun compare(
        feature: Feature,
        left: Region,
        right: Region
    ): ComparisonResult.ComparisonAlgorithmResult {
        require(feature is MarkerFeature) {
            "Feature is not MarkerFeature"
        }
        val sequences = getNormalizedAlignments(left.sequence, right.sequence)
        val repeatMotif = feature.repeatMotif

        val firstMatcher = repeatMotif.matcher(sequences.first)
        var firstNum = 0
        while (firstMatcher.find()) {
            firstNum++
        }

        val secondMatcher = repeatMotif.matcher(sequences.second)
        var secondNum = 0
        while (secondMatcher.find()) {
            secondNum++
        }

        val similarityPercent =
            min(firstNum, secondNum).toDouble() / max(firstNum, secondNum).toDouble() * 100.0
        val errorRate = 100 - calculateAverageQuality(listOf(*left.qualities, *right.qualities))

        return ComparisonResult.ComparisonAlgorithmResult(feature, similarityPercent, errorRate)
    }
}