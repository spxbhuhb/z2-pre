/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir

import hu.simplexion.z2.commons.kotlin.ir.CommonsPluginContext.Companion.PROPERTY_NAME_INDEX
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsManglerIr.fqnString
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.util.statements

class InterfaceTransform(
   val pluginContext: CommonsPluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = pluginContext.irContext.irBuiltIns

    fun IrProperty.message(message : String) = "localization interface property $message ${fqnString(true)}"

    /**
     * Set the last argument of the 'static' call to the name of the property.
     */
    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (declaration.name.identifier == CommonsPluginContext.LOCALIZATION_NAMESPACE) return declaration
        if (declaration.isFakeOverride) return declaration

        val getter = declaration.getter
        requireNotNull(getter) { declaration.message("getter must exists") }

        val ret = getter.body?.statements?.firstOrNull()
        require(ret != null && ret is IrReturn) { declaration.message("getter must have a return value") }

        val call = ret.value
        require(call is IrCall && call.symbol.owner.name.identifier == "static") { declaration.message("must be a call to 'static'") }

        call.putValueArgument(PROPERTY_NAME_INDEX, declaration.name.identifier.toIrConst(irBuiltIns.stringType))

        pluginContext.resources += "direct//${declaration.name.identifier}"

        return declaration
    }

}
