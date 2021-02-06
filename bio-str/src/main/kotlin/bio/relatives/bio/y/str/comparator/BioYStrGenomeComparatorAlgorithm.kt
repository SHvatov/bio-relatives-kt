package bio.relatives.bio.y.str.comparator

import bio.relatives.bio.y.str.model.MarkerFeature
import bio.relatives.common.comparator.GenomeComparatorAlgorithm
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.utils.getNormalizedAlignments
import bio.relatives.common.utils.round
import java.lang.Integer.min
import java.lang.Math.max
import java.util.regex.Matcher

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioYStrGenomeComparatorAlgorithm : GenomeComparatorAlgorithm {
    override fun compare(left: Region, right: Region, feature: Feature): ComparisonResult.ComparisonAlgorithmResult {
        require(feature is MarkerFeature) {
            "Feature is not MarkerFeature"
        }
        val sequences = getNormalizedAlignments(left.sequence, right.sequence)
        val repeatMotif = feature.repeatMotif

        val firstMatcher: Matcher = repeatMotif.matcher(sequences.first)
        var firstNum = 0
        while (firstMatcher.find()) {
            firstNum++
        }

        val secondMatcher: Matcher = repeatMotif.matcher(sequences.second)
        var secondNum = 0
        while (secondMatcher.find()) {
            secondNum++
        }

        val similarityPercent = round(min(firstNum, secondNum).toDouble()
                / max(firstNum, secondNum).toDouble() * 100.0, 2)
        val errorRate = round(100 - arrayOf(*left.qualities, *right.qualities).average(), 2)

        return ComparisonResult.ComparisonAlgorithmResult(feature, similarityPercent, errorRate)
    }
}