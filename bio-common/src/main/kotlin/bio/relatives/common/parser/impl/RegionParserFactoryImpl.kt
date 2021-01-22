package bio.relatives.common.parser.impl

import bio.relatives.common.parser.RegionParser
import bio.relatives.common.parser.RegionParserFactory
import htsjdk.samtools.SamReaderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author shvatov
 */
@Component("RegionParserFactory")
class RegionParserFactoryImpl @Autowired constructor(
    private val samReaderFactory: SamReaderFactory
) : RegionParserFactory {
    override fun create(bamFilePath: Path): RegionParser =
        RegionParserImpl(bamFilePath, samReaderFactory)
}