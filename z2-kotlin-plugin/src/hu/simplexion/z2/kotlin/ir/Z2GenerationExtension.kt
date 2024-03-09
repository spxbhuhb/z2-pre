/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.ir.css.CssModuleTransform
import hu.simplexion.z2.kotlin.ir.css.CssPluginContext
import hu.simplexion.z2.kotlin.ir.localization.LocalizationPluginContext
import hu.simplexion.z2.kotlin.ir.localization.ModuleTransform
import hu.simplexion.z2.kotlin.ir.localization.export.ExportResources
import hu.simplexion.z2.kotlin.ir.rui.RuiGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

internal class Z2GenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        localization(moduleFragment, pluginContext)
        // css(moduleFragment, pluginContext)
        RuiGenerationExtension(options).generate(moduleFragment, pluginContext)
    }

    fun localization(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        LocalizationPluginContext(options, pluginContext).apply {
            moduleFragment.accept(ModuleTransform(this), null)
            ExportResources(moduleFragment, this).export()
        }
    }

    fun css(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        CssPluginContext(options, pluginContext).apply {
            moduleFragment.accept(CssModuleTransform(this), null)
        }
    }

}

