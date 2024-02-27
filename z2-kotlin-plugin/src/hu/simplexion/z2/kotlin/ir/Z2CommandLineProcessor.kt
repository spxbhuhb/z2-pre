/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.Z2Options.Companion.OPTION_RESOURCE_DIR
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
class Z2CommandLineProcessor : CommandLineProcessor {

    override val pluginId = "z2"

    override val pluginOptions = listOf(
        OPTION_RESOURCE_DIR
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option) {
            OPTION_RESOURCE_DIR -> configuration.put(Z2Options.CONFIG_KEY_RESOURCE_DIR, File(value))
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
    }

}