package bio.relatives.bio.distance.comparator

import bio.relatives.common.comparator.GenomeComparisonMethod
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.utils.calculateAverageQuality
import bio.relatives.common.utils.getNormalizedAlignments
import org.springframework.stereotype.Component
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
@Component("LevensteinDistanceAlgorithm")
class LevensteinDistanceAlgorithm : GenomeComparisonMethod {

    override fun compare(feature: Feature, left: Region, right: Region): ComparisonResult.ComparisonAlgorithmResult {

        val temp = getNormalizedAlignments(left.sequence, right.sequence)

        val (first, second) = temp

        if (first.isEmpty() || second.isEmpty()) {
            return ComparisonResult.ComparisonAlgorithmResult(feature, 0.0, -1.0)
        }

        var table = IntArray(second.length + 1)

        for (i in 0 until second.length + 1) {
            table[i] = i
        }

        val current = IntArray(second.length + 1)
        for (i in 1 until first.length + 1) {
            current[0] = i
            for (j in 1 until second.length + 1) {
                current[j] = min(
                    min(current[j - 1] + 1, table[j] + 1),
                    min(table[j] + 1, table[j - 1] + if (first[i - 1] == second[j - 1]) 0 else 1)
                )
            }
            table = current.copyOf(current.size)
        }

        val difference = current[second.length]
        val length = max(first.length, second.length)
        val similarityPercent = 100.0 - difference.toDouble() / length.toDouble() * 100.0
        val errorRate = 100 - calculateAverageQuality(listOf(*left.qualities, *right.qualities))

        return ComparisonResult.ComparisonAlgorithmResult(feature, similarityPercent, errorRate)
    }
}