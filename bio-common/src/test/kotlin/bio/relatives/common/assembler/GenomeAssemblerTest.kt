package bio.relatives.common.assembler

import bio.relatives.common.FeatureTestImpl
import bio.relatives.common.assembler.impl.GenomeAssemblerImpl
import bio.relatives.common.model.Feature
import bio.relatives.common.model.Region
import bio.relatives.common.model.RoleAware.Role
import bio.relatives.common.parser.FeatureParser
import bio.relatives.common.parser.RegionParser
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author shvatov
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
internal class GenomeAssemblerImplTest {
    @Mock
    private lateinit var featureParser: FeatureParser

    @Mock
    private lateinit var regionParser: RegionParser

    @Mock
    private lateinit var regionAssembler: RegionAssembler

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var assemblyCtx: AssemblyCtx

    @BeforeEach
    private fun setUp() {
        // bed setup
        whenever(assemblyCtx.featureFilePath).thenReturn(MOCK_FEATURE_FILE_PATH)
        whenever(assemblyCtx.featureParser).thenReturn(featureParser)
        whenever(featureParser.parseFeatures(MOCK_FEATURE_FILE_PATH))
            .thenReturn(MOCK_FEATURES)

        // bam setup
        whenever(assemblyCtx.bamFilePaths).thenReturn(
            mapOf(
                Role.FATHER to MOCK_FATHER_FILE_PATH,
                Role.MOTHER to MOCK_MOTHER_FILE_PATH,
                Role.SON to MOCK_SON_FILE_PATH,
            )
        )
        whenever(assemblyCtx.regionAssemblerFactory.create())
            .thenReturn(regionAssembler)
        whenever(assemblyCtx.regionParserFactory.create(any()))
            .thenReturn(regionParser)
        whenever(regionParser.parseRegion(any()))
            .thenReturn(emptyList())
        whenever(regionAssembler.assemble(any(), any(), any()))
            .thenAnswer {
                val role = it.arguments[0] as Role
                val feature = it.arguments[1] as Feature
                Region(
                    role,
                    MOCK_SEQ,
                    MOCK_SEQ_QUALITY,
                    feature.gene,
                    feature.chromosome,
                    feature.start,
                    feature.end
                )
            }
    }

    @Test
    fun `successful run`() = runBlocking {
        GenomeAssemblerImpl(assemblyCtx, mock()).use {
            val outputChannel = it.assemble()
            val retrievedData = mutableListOf<GenomeAssemblyResult>()
            outputChannel.consumeEach {
                retrievedData.add(it)
            }

            assertEquals(MOCK_FEATURES.size, retrievedData.size)
            retrievedData.forEach {
                assertTrue { it.isSuccess }
                assertTrue { it.result != null }
                for (elem in it.result?.regionsByRole?.values ?: emptyList()) {
                    assertEquals(MOCK_SEQ, elem.sequence)
                    assertEquals(MOCK_SEQ_QUALITY, elem.qualities)
                }
            }
        }
    }

    private companion object {
        val MOCK_FEATURE_FILE_PATH: Path = Paths.get("some/path")

        val MOCK_FATHER_FILE_PATH: Path = Paths.get("father/path")

        val MOCK_MOTHER_FILE_PATH: Path = Paths.get("mother/path")

        val MOCK_SON_FILE_PATH: Path = Paths.get("child/path")

        const val MOCK_GENE = "gene"

        const val MOCK_CHROM = "chr1"

        const val MOCK_SEQ = "agct"

        val MOCK_SEQ_QUALITY = arrayOf(0.99, 0.97, 0.95, 0.94)

        val MOCK_FEATURES = (0..100 step 10).map {
            FeatureTestImpl(MOCK_GENE, MOCK_CHROM, it + 1, it + 10)
        }
    }
}