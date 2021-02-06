package bio.relatives.common.cli.runner

import bio.relatives.common.model.ComparatorType
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 05.02.2021
 */
interface Runner {
    fun run(
            comparatorType: ComparatorType,
            pathToFeatureFile: Path,
            pathToFatherFile: Path,
            pathToMotherFile: Path,
            pathToSonFile: Path
    )
}