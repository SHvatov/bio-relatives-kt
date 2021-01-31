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
        else -> 0
    }
}