package bio.relatives.runner

import bio.relatives.common.cli.runner.Runner
import bio.relatives.common.cli.runner.RunnerFactory
import bio.relatives.common.model.ComparatorType
import org.springframework.stereotype.Component

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
@Component
class RunnerFactoryImpl : RunnerFactory {
    override fun create(comparatorType: ComparatorType): Runner {
        return if (comparatorType == ComparatorType.NAIVE) {
            NaiveComparisonRunner()
        } else {
            YStrComparisonRunner()
        }
    }
}