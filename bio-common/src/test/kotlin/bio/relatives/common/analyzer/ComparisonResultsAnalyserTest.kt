package bio.relatives.common.analyzer

import bio.relatives.common.FeatureTestImpl
import bio.relatives.common.analyzer.impl.ComparisonResultsAnalyserImpl
import bio.relatives.common.comparator.GenomeComparisonResult
import bio.relatives.common.model.ComparisonParticipants.Companion.COMPARISON_PAIRS
import bio.relatives.common.model.ComparisonResult
import bio.relatives.common.model.ComparisonResult.ComparisonAlgorithmResult
import bio.relatives.common.model.Feature
import com.shvatov.processor.data.TaskCompletionState
import com.shvatov.processor.data.TaskIdentifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
internal class ComparisonResultsAnalyserTest {
    @Test
    fun `successful run`() = runBlocking {
        val channel = Channel<GenomeComparisonResult>(capacity = COMPARISON_DATA.size)
        launch {
            COMPARISON_DATA.forEach {
                channel.send(it)
            }
            channel.close()
        }

        val result = ComparisonResultsAnalyserImpl(channel, this).analyse()
        result.forEach { (_, value) ->
            assertEquals(MOCK_SIMILARITY, value.averageSimilarity, 1e-6)
            assertEquals(1, value.chromosomeResults.size)
        }
    }

    private companion object {
        fun prepareComparisonDataMock(feature: Feature): GenomeComparisonResult {
            return GenomeComparisonResult(
                identifier = TaskIdentifier(UUID.randomUUID(), UUID.randomUUID()),
                completionState = TaskCompletionState.SUCCESS,
                result = COMPARISON_PAIRS.associateWith {
                    ComparisonAlgorithmResult(
                        feature,
                        MOCK_SIMILARITY,
                        MOCK_ERROR
                    )
                }.toMap(ComparisonResult())
            )
        }

        const val MOCK_GENE = "gene"

        const val MOCK_CHROM = "chr1"

        const val MOCK_SIMILARITY = 0.99

        const val MOCK_ERROR = 0.011

        val MOCK_FEATURES = (0..100 step 10).map {
            FeatureTestImpl(MOCK_GENE, MOCK_CHROM, it + 1, it + 10)
        }

        val COMPARISON_DATA = MOCK_FEATURES.map { prepareComparisonDataMock(it) }
    }
}