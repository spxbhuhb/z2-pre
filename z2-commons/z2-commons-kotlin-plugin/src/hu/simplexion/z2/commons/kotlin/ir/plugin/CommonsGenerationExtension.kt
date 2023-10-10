/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir.plugin

import hu.simplexion.z2.commons.kotlin.ir.CommonsModuleTransform
import hu.simplexion.z2.commons.kotlin.ir.CommonsPluginContext
import hu.simplexion.z2.commons.kotlin.ir.export.ExportResources
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

internal class CommonsGenerationExtension(
    val options : CommonsOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        CommonsPluginContext(options, pluginContext).apply {

//            debug("commons") { "====  START  ==".padEnd(80, '=') }

            // order is important here
            moduleFragment.accept(CommonsModuleTransform(this), null)

            ExportResources(moduleFragment, this).export()

//            debug("commons") { moduleFragment.dump() }
//            debug("commons") { "====  END  ====".padEnd(80, '=') }
        }
    }
}

