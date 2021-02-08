package bio.relatives.common.cli.runner

import bio.relatives.common.model.ComparatorType
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
data class RunnerCtx(
        val comparatorType: ComparatorType,
        val pathToFeatureFile: Path,
        val pathToFatherFile: Path,
        val pathToMotherFile: Path,
        val pathToSonFile: Path
)
