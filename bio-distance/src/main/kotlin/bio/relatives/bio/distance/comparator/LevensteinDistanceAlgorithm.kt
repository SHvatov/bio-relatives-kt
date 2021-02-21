package bio.relatives.bio.distance.comparator

import bio.relatives.common.comparator.GenomeComparisonMethod
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.utils.calculateAverageQuality
import bio.relatives.common.utils.getNormalizedAlignments
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class LevensteinDistanceAlgorithm : GenomeComparisonMethod {

    override fun compare(feature: Feature, left: Region, right: Region): ComparisonResult.ComparisonAlgorithmResult {

        val temp = getNormalizedAlignments(left.sequence, right.sequence)

        val (f, s) = temp

        var table = IntArray(s.length + 1)

        for (l in 0 until s.length + 1) {
            table[l] = l
        }

        val current = IntArray(s.length + 1)
        for (l in 1 until f.length + 1) {
            current[0] = l
            for (k in 1 until s.length + 1) {
                current[k] = min(min(current[k - 1] + 1, table[k] + 1), min(table[k] + 1, table[k - 1] + if (f[l - 1] == s[k - 1]) 0 else 1))
            }
            table = current.copyOf(current.size)
        }

        val difference = current[s.length]
        val length = max(f.length, s.length)
        val similarityPercent = 100.0 - difference.toDouble() / length.toDouble() * 100.0
        val errorRate = 100 - calculateAverageQuality(listOf(*left.qualities, *right.qualities))

        return ComparisonResult.ComparisonAlgorithmResult(feature, similarityPercent, errorRate)
    }

}