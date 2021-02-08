package bio.relatives.common.model

import java.util.regex.Pattern

/**
 * @author Created by Vladislav Marchenko on 31.01.2021
 */
interface RepeatMotifAware {
    val repeatMotif: Pattern
}