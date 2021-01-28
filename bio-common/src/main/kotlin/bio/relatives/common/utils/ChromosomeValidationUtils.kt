package bio.relatives.common.utils

import bio.relatives.common.model.ChromosomeAware
import bio.relatives.common.model.ChromosomePositionAware
import bio.relatives.common.model.NucleotideSequenceAware
import java.util.regex.Pattern

/**
 * @author shvatov
 */
fun validateChrom(aware: ChromosomeAware) {
    with(aware) {
        require(chromosome.isNotBlank() && gene.isNotBlank()) {
            "Both \"chromosome\" and \"gene\" must be present"
        }

        require(GENE_NAME_PATTERN.matcher(gene).matches()) {
            "\"gene\" must match the following pattern: \"[a-zA-Z0-9.\\\\-_+]*\""
        }

        require(chromosome in ALLOWED_CHROMOSOMES) {
            "\"chromosome\" must be one of the following: " +
                ALLOWED_CHROMOSOMES.joinToString()
        }
    }
}

fun validateChromPosition(aware: ChromosomePositionAware) {
    with(aware) {
        require(start in 0 until end) {
            "\"start must be from 0 to \"end\" exclusive"
        }
    }
}

fun validateNucleotideSequence(aware: NucleotideSequenceAware) {
    with(aware) {
        require(sequence.all { it in ALLOWED_NUCLEOTIDES }) {
            "\"sequence\" must be formed from valid nucleotides - " +
                    ALLOWED_NUCLEOTIDES.toCharArray().joinToString()
        }
    }
}

const val ALLOWED_NUCLEOTIDES = "agct"

const val UNKNOWN_NUCLEOTIDE = '*'

val ALLOWED_CHROMOSOMES = (1..22).map { "chr$it" } + listOf("chrX", "chrY", "chrMT")

val GENE_NAME_PATTERN: Pattern = Pattern.compile("[a-zA-Z0-9.\\-_+]*")
