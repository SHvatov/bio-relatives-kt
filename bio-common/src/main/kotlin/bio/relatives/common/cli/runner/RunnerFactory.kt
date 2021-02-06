package bio.relatives.common.cli.runner

import bio.relatives.common.model.ComparatorType

/**
 * @author Created by Vladislav Marchenko on 06.02.2021
 */
interface RunnerFactory {
    fun create(comparatorType: ComparatorType): Runner
}