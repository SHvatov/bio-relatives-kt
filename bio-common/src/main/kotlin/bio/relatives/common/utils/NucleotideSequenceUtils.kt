package bio.relatives.common.utils

import kotlin.math.min

/**
 * @author Created by Vladislav Marchenko on 03.02.2021
 */

/**
 * Discards sections with unknown nucleotides from the sequences ([first] and [second])
 */
fun getNormalizedAlignments(first: String, second: String): Pair<String, String> {

    val firstGenome = StringBuilder()
    val secondGenome = StringBuilder()
    for (i in 0 until min(first.length, second.length)) {

        if (!isUnknownNucleotide(first[i]) && !isUnknownNucleotide(second[i])) {
            firstGenome.append(first[i])
            secondGenome.append(second[i])
        }
    }

    return Pair(firstGenome.toString(), secondGenome.toString())
}

/**
 * Determines if the [nucleotide] is unknown
 */
private fun isUnknownNucleotide(nucleotide: Char): Boolean = nucleotide == UNKNOWN_NUCLEOTIDE

