package bio.relatives.runner

import bio.relatives.common.cli.runner.Runner
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component("YStrComparisonRunner")
class YStrComparisonRunner : Runner {
    override fun run(pathToFeatureFile: Path, pathToFatherFile: Path, pathToMotherFile: Path, pathToSonFile: Path) {
        TODO("Not yet implemented")
    }
}