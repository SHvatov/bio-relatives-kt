package bio.relatives.common.utils

import kotlin.math.min

/**
 * @author Created by Vladislav Marchenko on 03.02.2021
 */

/**
 * Sets the sequences ([first] and [second]) to the same length
 * and discards sections with unknown nucleotides from them
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

    if (firstGenome.length < secondGenome.length) {
        for (i in first.length until second.length) {
            if (!isUnknownNucleotide(second[i])) {
                secondGenome.append(i)
            }
        }
    } else {
        for (i in second.length until first.length) {
            if (!isUnknownNucleotide(first[i])) {
                firstGenome.append(i)
            }
        }
    }
    return Pair(firstGenome.toString(), secondGenome.toString())
}

/**
 * Determines if the [nucleotide] is unknown
 */
private fun isUnknownNucleotide(nucleotide: Char): Boolean = nucleotide == UNKNOWN_NUCLEOTIDE

