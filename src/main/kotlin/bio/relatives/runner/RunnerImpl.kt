package bio.relatives.runner

import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.model.ComparatorType
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
class RunnerImpl : Runner {

    override fun run(comparatorType: ComparatorType, pathToFeatureFile: Path, pathToFatherFile: Path, pathToMotherFile: Path, pathToSonFile: Path) {
        print("Hello")
    }
}