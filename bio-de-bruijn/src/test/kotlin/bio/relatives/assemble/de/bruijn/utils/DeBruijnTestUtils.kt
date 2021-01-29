package bio.relatives.assemble.de.bruijn.utils

/**
 * @author Created by Vladislav Marchenko on 29.01.2021
 */

val TEST_GENOME_START = 0

val TEST_GENOME_END = 9

val TEST_CHR = "chr1"

val TEST_GENE = "gene1"

val TEST_READ_LENGTH_1 = 5
val TEST_READ_LENGTH_2 = 5
val TEST_READ_LENGTH_3 = 5
val TEST_READ_LENGTH_4 = 5
val TEST_READ_LENGTH_5 = 6
val TEST_READ_LENGTH_6 = 5


val TEST_READ_STRING_1 = "agcta"
val TEST_READ_STRING_2 = "ggcta"
val TEST_READ_STRING_3 = "ctagc"
val TEST_READ_STRING_4 = "tagat"
val TEST_READ_STRING_5 = "tagcta"
val TEST_READ_STRING_6 = "gctat"

val TEST_READ_START_1 = 0
val TEST_READ_START_2 = 0
val TEST_READ_START_3 = 2
val TEST_READ_START_4 = 3
val TEST_READ_START_5 = 3
val TEST_READ_START_6 = 5

val TEST_READ_END_1 = 4
val TEST_READ_END_2 = 4
val TEST_READ_END_3 = 6
val TEST_READ_END_4 = 7
val TEST_READ_END_5 = 8
val TEST_READ_END_6 = 9

val TEST_BASE_QUALITIES_1 = arrayOf<Byte>(
        25, 30, 40, 50, 60
).toByteArray()
val TEST_BASE_QUALITIES_2 = arrayOf<Byte>(
        10, 70, 20, 30, 10
).toByteArray()
val TEST_BASE_QUALITIES_3 = arrayOf<Byte>(
        10, 50, 51, 52, 53
).toByteArray()
val TEST_BASE_QUALITIES_4 = arrayOf<Byte>(
        54, 55, 56, 90, 100
).toByteArray()
val TEST_BASE_QUALITIES_5 = arrayOf<Byte>(
        10, 11, 21, 10, 21, 10
).toByteArray()
val TEST_BASE_QUALITIES_6 = arrayOf<Byte>(
        10, 5, 3, 2, 1
).toByteArray()