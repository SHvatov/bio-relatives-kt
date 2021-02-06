package bio.relatives.bio.distance.comparator

import bio.relatives.common.comparator.GenomeComparatorAlgorithm
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.utils.getNormalizedAlignments
import bio.relatives.common.utils.round
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioDistanceGenomeComparatorAlgorithm : GenomeComparatorAlgorithm {

    override fun compare(left: Region, right: Region, feature: Feature): ComparisonResult.ComparisonAlgorithmResult {

        val temp: Pair<String, String> = getNormalizedAlignments(left.sequence, right.sequence)

        val f: String = temp.first
        val s: String = temp.second


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
        val similarityPercent = round(100.0 - difference.toDouble() / length.toDouble() * 100.0, 2)
        val errorRate = round(100 - arrayOf(*left.qualities, *right.qualities).average(), 2)

        return ComparisonResult.ComparisonAlgorithmResult(feature, similarityPercent, errorRate)
    }

}