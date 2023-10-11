/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import hu.simplexion.z2.kotlin.ir.localization.LocalizationPluginContext.Companion.STATIC_NAME_ARG_INDEX
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsManglerIr.fqnString
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.util.statements

class InterfaceTransform(
   val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = pluginContext.irContext.irBuiltIns

    fun IrProperty.message(message : String) = "localization interface property $message ${fqnString(true)}"

    /**
     * Set the last argument of the 'static' call to the name of the property.
     */
    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (transformNamespace(declaration, pluginContext)) return declaration
        if (declaration.isFakeOverride) return declaration

        val getter = declaration.getter
        requireNotNull(getter) { declaration.message("getter must exists") }

        val ret = getter.body?.statements?.firstOrNull()
        require(ret != null && ret is IrReturn) { declaration.message("getter must have a return value") }

        val call = ret.value
        require(call is IrCall && call.symbol.owner.name.identifier == "static") { declaration.message("must be a call to 'static'") }

        call.putValueArgument(STATIC_NAME_ARG_INDEX, declaration.name.identifier.toIrConst(irBuiltIns.stringType))

        pluginContext.register(declaration, declaration.name, call)

        return declaration
    }

}
