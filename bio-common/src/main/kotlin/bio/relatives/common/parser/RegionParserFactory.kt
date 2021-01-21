package bio.relatives.common.parser

import java.nio.file.Path
import java.util.UUID

/**
 * @author shvatov
 */
interface RegionParserFactory {
    fun create(personIdentifier: UUID, bamFilePath: Path): RegionParser
}