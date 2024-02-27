package hu.simplexion.z2.kotlin

import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import java.io.File

class Z2Options(
    val resourceOutputDir : File
) {
    companion object {

        // -------------------------------------------------------------------------------------------------
        // Resource dir
        // -------------------------------------------------------------------------------------------------

        const val OPTION_NAME_RESOURCE_DIR = "resource-output-dir"

        val CONFIG_KEY_RESOURCE_DIR = CompilerConfigurationKey.create<File>(OPTION_NAME_RESOURCE_DIR)

        val OPTION_RESOURCE_DIR = CliOption(
            OPTION_NAME_RESOURCE_DIR, "string", "Path to the directory to write generated resources into.",
            required = true, allowMultipleOccurrences = false
        )
    }
}