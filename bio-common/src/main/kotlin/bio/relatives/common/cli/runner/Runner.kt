package bio.relatives.common.cli.runner

import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 05.02.2021
 */
interface Runner {
    fun run(
            pathToFeatureFile: Path,
            pathToFatherFile: Path,
            pathToMotherFile: Path,
            pathToSonFile: Path
    )
}