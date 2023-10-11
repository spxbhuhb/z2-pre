/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.isEnumClass
import org.jetbrains.kotlin.ir.util.isInterface
import org.jetbrains.kotlin.ir.util.isObject
import org.jetbrains.kotlin.ir.util.isSubclassOf

class ModuleTransform(
    private val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        if (! declaration.isSubclassOf(pluginContext.localizationProvider)) {
            return declaration
        }

        when {
            declaration.isEnumClass -> declaration.accept(EnumTransform(pluginContext), null)
            declaration.isObject -> declaration.accept(ObjectTransform(pluginContext), null)
            declaration.isInterface -> declaration.accept(InterfaceTransform(pluginContext), null)
            else -> declaration.accept(ClassTransform(pluginContext), null)
        }

        return declaration
    }

}
