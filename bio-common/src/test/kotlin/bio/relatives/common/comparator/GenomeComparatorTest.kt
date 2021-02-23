package bio.relatives.common.comparator

import bio.relatives.common.FeatureTestImpl
import bio.relatives.common.assembler.GenomeAssemblyResult
import bio.relatives.common.comparator.impl.GenomeComparatorImpl
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RegionBatch
import bio.relatives.common.model.RoleAware.Role
import com.shvatov.processor.data.TaskCompletionState
import com.shvatov.processor.data.TaskIdentifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
internal class GenomeComparatorTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var compareCtx: CompareCtx

    @Test
    fun `successful run`() = runBlocking {
        val channel = Channel<GenomeAssemblyResult>(capacity = ASSEMBLY_DATA.size)
        launch {
            ASSEMBLY_DATA.forEach {
                channel.send(it)
            }
            channel.close()
        }

        GenomeComparatorImpl(compareCtx, channel, this).run {
            val outputChannel = compare()
            val retrievedData = mutableListOf<GenomeComparisonResult>()
            outputChannel.consumeEach {
                retrievedData.add(it)
            }

            assertEquals(MOCK_FEATURES.size, retrievedData.size)
            retrievedData.forEach {
                assertTrue { it.isSuccess }
                assertTrue { it.result != null }
            }
        }
    }

    private companion object {
        fun prepareAssemblyDataMock(feature: Feature): GenomeAssemblyResult {
            return GenomeAssemblyResult(
                identifier = TaskIdentifier(UUID.randomUUID(), UUID.randomUUID()),
                completionState = TaskCompletionState.SUCCESS,
                result = RegionBatch(
                    feature,
                    enumValues<Role>().associateWith {
                        Region(
                            it,
                            MOCK_SEQ,
                            MOCK_SEQ_QUALITY,
                            feature.gene,
                            feature.chromosome,
                            feature.start,
                            feature.end
                        )
                    }
                )
            )
        }

        const val MOCK_GENE = "gene"

        const val MOCK_CHROM = "chr1"

        const val MOCK_SEQ = "agct"

        val MOCK_SEQ_QUALITY = arrayOf(0.99, 0.97, 0.95, 0.94)

        val MOCK_FEATURES = (0..100 step 10).map {
            FeatureTestImpl(MOCK_GENE, MOCK_CHROM, it + 1, it + 10)
        }

        val ASSEMBLY_DATA = MOCK_FEATURES.map { prepareAssemblyDataMock(it) }
    }
}