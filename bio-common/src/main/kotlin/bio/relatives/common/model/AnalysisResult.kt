package bio.relatives.common.model

import bio.relatives.common.model.AnalysisResult.GenomeResult
import bio.relatives.common.model.ComparisonParticipants.Companion.COMPARISON_PAIRS
import java.io.OutputStream
import kotlin.math.abs

/**
 * @author shvatov
 */
class AnalysisResult : MutableMap<ComparisonParticipants, GenomeResult> by HashMap() {
    /**
     * Result of the comparison of the whole genomes. Stores the [averageSimilarity]
     * and [averageErrorRate] of the genomes. Also stores some detailed information
     * about each chromosome.
     */
    data class GenomeResult(
        val averageSimilarity: Double,
        val averageErrorRate: Double,
        val chromosomeResults: List<ChromosomeResult>
    )

    /**
     * Result of the comparison of two chromosomes. Stores the [averageSimilarity]
     * and [averageErrorRate] of the chromosomes.
     */
    data class ChromosomeResult(
        override val gene: String,
        override val chromosome: String,
        val averageSimilarity: Double,
        val averageErrorRate: Double,
    ) : ChromosomeAware

    /**
     * Pretty prints the snapshot of all the comparison results the provided [output].
     */
    fun presentResults(output: OutputStream = System.out) =
        COMPARISON_PAIRS.forEach {
            presentResult(output, it)
        }

    /**
     * Pretty prints the snapshot of  the comparison results of two [participants]
     * to the provided [output].
     */
    private fun presentResult(output: OutputStream, participants: ComparisonParticipants) {
        val resultsSnapshot = this[participants] ?: return
        output.use {
            val builder = StringBuilder()

            participants.run {
                builder.append(
                    "Results of the comparison between: " +
                        "${firstRole.representation} and ${secondRole.representation}\n"
                )
            }

            resultsSnapshot.run {
                builder.append("\tWhole provided genome comparison results:\n")
                builder.append("\t\tAverage similarity among all chromosomes: $averageSimilarity\n")
                builder.append("\t\tError rate: $averageErrorRate\n\n")
            }

            builder.append("\tComparison results for each chromosome independently:\n")
            builder.append("\t\tChromosomes processed: ${resultsSnapshot.chromosomeResults.size}\n\n")
            for (chromosome in resultsSnapshot.chromosomeResults) {
                chromosome.run {
                    builder.append("\t\tGene: $gene\n")
                    builder.append("\t\tChromosome: $chromosome\n")
                    builder.append("\t\tAverage similarity: $averageSimilarity\n")
                    builder.append("\t\tError rate: $averageErrorRate\n")
                }
            }

            builder.append(
                "\nConclusion: at the moment similarity threshold at which " +
                    "we consider two persons being relatives is: [$RELATIVES_THRESHOLD]\n"
            )

            val areRelatives = abs(resultsSnapshot.averageSimilarity - RELATIVES_THRESHOLD) < COMPARISON_DELTA

            builder.append(
                "\nCurrent similarity percentage is ${resultsSnapshot.averageSimilarity}, " +
                    "meaning that they are ${(if (!areRelatives) "not" else "") + " relatives"}\n"
            )

            it.write(builder.toString().toByteArray())
        }
    }

    private companion object {
        const val RELATIVES_THRESHOLD = 99.9

        const val COMPARISON_DELTA = 1e-5
    }
}