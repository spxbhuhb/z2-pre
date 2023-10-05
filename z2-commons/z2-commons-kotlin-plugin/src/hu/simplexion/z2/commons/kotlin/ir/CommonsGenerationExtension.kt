/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

internal class CommonsGenerationExtension : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        CommonsPluginContext(pluginContext).apply {

//            debug("commons") { "====  START  ==".padEnd(80, '=') }

            // order is important here
            moduleFragment.accept(CommonsModuleTransform(this), null)

//            debug("commons") { moduleFragment.dump() }
//            debug("commons") { "====  END  ====".padEnd(80, '=') }
        }
    }
}

