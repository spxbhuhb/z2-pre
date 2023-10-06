/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.isSubclassOf

class CommonsModuleTransform(
    private val pluginContext: CommonsPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        if (declaration.isSubclassOf(pluginContext.localizationProvider)) {
            declaration.accept(LocalizationProviderTransform(pluginContext), null)
        }

        return declaration
    }

}
