/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir.plugin

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
class CommonsCommandLineProcessor : CommandLineProcessor {

    override val pluginId = "z2-commons"

    override val pluginOptions: Collection<AbstractCliOption> = emptyList()

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option) {
            CommonsOptions.OPTION_RESOURCE_DIR -> configuration.put(CommonsOptions.CONFIG_KEY_RESOURCE_DIR, File(value))
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
    }

}