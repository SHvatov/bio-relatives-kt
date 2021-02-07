package bio.relatives.common.utils

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Based on the provided [values] calculates the median value.
 * @author Created by Vladislav Marchenko on 28.01.2021
 */
fun <T : Number> calculateMedianVale(values: List<T>): Double =
    with(values.map { it.toDouble() }.sorted()) {
        val middle = size / 2
        return@with when {
            size % 2 != 0 -> get(middle)
            isNotEmpty() -> (get(middle) - get(middle - 1)) / 2.0
            else -> 0.0
        }
    }

/**
 * For provided [values] array calculates the root mean square value.
 */
fun <T : Number> calculateRootMeanSquare(values: List<T>): Double {
    values.ifEmpty { return 0.0 }

    val squareSum = values.map { it.toDouble().pow(2) }.sum()
    return sqrt(squareSum / values.size)
}

/**
 * Returns the average quality value of the [values] list.
 */
fun <T : Number> calculateAverageQuality(values: List<T>): Double {
    return values.map { it.toDouble() }.sum() / values.size
}

/**
 * Takes a map, where key is some arithmetic value, and value is its calculation error,
 * and calculates the relative error rate of addition of all this values.
 */
fun <T : Number> calculateAdditionRelativeErrorRate(valueToError: Map<T, T>): Double {
    return valueToError.map { it.value.toDouble() }.sum() /
            valueToError.map { it.key.toDouble() }.sum()
}

/**
 * Rounds a [valueToRound] to [placesNum] decimal places
 */
fun round(valueToRound: Double, placesNum: Byte): Double =
        (valueToRound * 10.0.pow(placesNum.toDouble())).roundToInt() / 10.0.pow(placesNum.toDouble())



