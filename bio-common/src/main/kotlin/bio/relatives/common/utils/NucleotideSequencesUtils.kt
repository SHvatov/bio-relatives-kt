package bio.relatives.common.utils

/**
 * @author Created by Vladislav Marchenko on 28.01.2021
 */

fun getMedianQuality(qualities: List<Byte>): Byte {

    qualities.sorted()

    return when {
        qualities.size % 2 != 0 -> {
            qualities[qualities.size / 2]
        }
        qualities.isNotEmpty() -> {
            ((qualities[qualities.size / 2] + qualities[qualities.size / 2 - 1]) / 2).toByte()
        }
        else -> {
            0
        }
    }
}

fun getNormalizedAlignments(first: String, second: String): Pair<String, String> {
    val firstGenome = StringBuilder()
    val secondGenome = StringBuilder()

    for (i in 0 until Integer.min(first.length, second.length)) {
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

fun isUnknownNucleotide(nucleotide: Char): Boolean = nucleotide == UNKNOWN_NUCLEOTIDE