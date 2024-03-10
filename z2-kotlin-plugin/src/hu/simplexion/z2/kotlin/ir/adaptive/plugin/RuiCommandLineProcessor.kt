/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.plugin

import hu.simplexion.z2.kotlin.ir.adaptive.*
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
class AdaptiveCommandLineProcessor : CommandLineProcessor {
    companion object {

        val OPTION_ANNOTATION = CliOption(
            OPTION_NAME_ANNOTATION, "<fqname>", "Annotation qualified names",
            required = false, allowMultipleOccurrences = true
        )
        val OPTION_DUMP = CliOption(
            OPTION_NAME_DUMP_POINT, "string", "Dump data at specified points of plugin run (${AdaptiveDumpPoint.optionValues().joinToString { ", " }}).",
            required = false, allowMultipleOccurrences = true
        )
        val OPTION_ROOT_NAME_STRATEGY = CliOption(
            OPTION_NAME_ROOT_NAME_STRATEGY, "string", "Select root name strategy (${AdaptiveRootNameStrategy.optionValues().joinToString { ", " }}",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_TRACE = CliOption(
            OPTION_NAME_TRACE, "boolean", "Add trace output to the generated code.",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_EXPORT_STATE = CliOption(
            OPTION_NAME_EXPORT_STATE, "boolean", "Generate state export functions",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_IMPORT_STATE = CliOption(
            OPTION_NAME_IMPORT_STATE, "boolean", "Generate state import functions",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_PRINT_DUMPS = CliOption(
            OPTION_NAME_PRINT_DUMPS, "boolean", "Use println for output instead of the compiler logging framework",
            required = false, allowMultipleOccurrences = false
        )
        val OPTION_PLUGIN_LOG_DIR = CliOption(
            OPTION_NAME_PLUGIN_LOG_DIR, "string", "Save plugin output into a file in this directory.",
            required = false, allowMultipleOccurrences = false
        )
    }

    override val pluginId = "z2" // FIXME hard-coded plugin name

    override val pluginOptions = listOf(
        OPTION_ANNOTATION,
        OPTION_DUMP,
        OPTION_ROOT_NAME_STRATEGY,
        OPTION_TRACE,
        OPTION_EXPORT_STATE,
        OPTION_IMPORT_STATE,
        OPTION_PRINT_DUMPS,
        OPTION_PLUGIN_LOG_DIR
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option) {
            OPTION_ANNOTATION -> configuration.appendList(AdaptiveConfigurationKeys.ANNOTATION, value)
            OPTION_DUMP -> configuration.appendList(AdaptiveConfigurationKeys.DUMP, value.toDumpPoint())
            OPTION_ROOT_NAME_STRATEGY -> configuration.put(AdaptiveConfigurationKeys.ROOT_NAME_STRATEGY, value.toRootNameStrategy())
            OPTION_TRACE -> configuration.put(AdaptiveConfigurationKeys.TRACE, value.toBooleanStrictOrNull() ?: false)
            OPTION_EXPORT_STATE -> configuration.put(AdaptiveConfigurationKeys.EXPORT_STATE, value.toBooleanStrictOrNull()
                ?: false)

            OPTION_IMPORT_STATE -> configuration.put(AdaptiveConfigurationKeys.IMPORT_STATE, value.toBooleanStrictOrNull()
                ?: false)

            OPTION_PRINT_DUMPS -> configuration.put(AdaptiveConfigurationKeys.PRINT_DUMPS, value.toBooleanStrictOrNull()
                ?: false)

            OPTION_PLUGIN_LOG_DIR -> configuration.put(AdaptiveConfigurationKeys.PLUGIN_LOG_DIR, value)
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
    }

    private fun String.toDumpPoint(): AdaptiveDumpPoint =
        AdaptiveDumpPoint.fromOption(this) ?: throw CliOptionProcessingException("Unknown dump point: $this")

    private fun String.toRootNameStrategy(): AdaptiveRootNameStrategy =
        AdaptiveRootNameStrategy.fromOption(this) ?: throw CliOptionProcessingException("Unknown root name strategy: $this")
}

