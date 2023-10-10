/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin

import hu.simplexion.z2.commons.kotlin.ir.plugin.CommonsGenerationExtension
import hu.simplexion.z2.commons.kotlin.ir.plugin.CommonsOptions
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

/**
 * Registers the extensions into the compiler.
 */
@OptIn(ExperimentalCompilerApi::class)
class CommonsCompilerPluginRegistrar : CompilerPluginRegistrar() {

    override val supportsK2 = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val options = CommonsOptions(
            resourceOutputDir = configuration.get(CommonsOptions.CONFIG_KEY_RESOURCE_DIR)!!
        )
        IrGenerationExtension.registerExtension(CommonsGenerationExtension(options))
    }

}
