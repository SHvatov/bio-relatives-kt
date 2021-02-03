package bio.relatives.bio.y.str.comparator

import bio.relatives.bio.y.str.model.BioYStrAlgorithmResult
import bio.relatives.common.comparator.GenomeComparatorAlgorithm
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.Region
import bio.relatives.common.utils.getNormalizedAlignments
import java.util.regex.Matcher

/**
 * @author Created by Vladislav Marchenko on 30.01.2021
 */
class BioYStrGenomeComparatorAlgorithm : GenomeComparatorAlgorithm {
    override fun compare(left: Region, right: Region): ComparisonResult.AlgorithmResult {
        val sequences = getNormalizedAlignments(left.sequence, right.sequence)

        val firstMatcher: Matcher = left.repeatMotif.matcher(sequences.first)
        var firstNum = 0
        while (firstMatcher.find()) {
            firstNum++
        }

        val secondMatcher: Matcher = right.repeatMotif.matcher(sequences.second)
        var secondNum = 0
        while (secondMatcher.find()) {
            secondNum++
        }

        return BioYStrAlgorithmResult(Pair(firstNum, secondNum))
    }
}