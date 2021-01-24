package bio.relatives.common.utils

import java.nio.file.Path

/**
 * @author shvatov
 */

/**
 * Checks, whether a file by provided path is a valid file or directory, that
 * exists and can be opened. [isFileRequired] determines, whether this path
 * is related to a file or directory. If [requiredExtension] is provided, and
 * [isFileRequired] set to true, then also checks the extension of the file.
 */
fun Path.isValid(
    isFileRequired: Boolean = true,
    requiredExtension: String? = null
): Boolean {
    if (!isFileRequired && requiredExtension != null) {
        throw IllegalArgumentException(
            "Incorrect arguments composition: [$isFileRequired, $requiredExtension]"
        )
    }

    return with(toFile()) {
        val isReadable = exists() && canRead()
        val isValidFileType = isFileRequired && isFile
            || !isFileRequired && isDirectory
        val isValidExtension = requiredExtension?.let {
            it == extension
        } ?: true
        isReadable && isValidFileType && isValidExtension
    }
}