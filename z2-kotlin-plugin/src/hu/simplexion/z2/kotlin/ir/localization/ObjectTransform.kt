/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import hu.simplexion.z2.kotlin.ir.localization.LocalizationPluginContext.Companion.PROPERTY_NAME_INDEX
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsManglerIr.fqnString
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.interpreter.toIrConst

class ObjectTransform(
    val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = pluginContext.irContext.irBuiltIns

    fun IrProperty.message(message : String) = "localization object property $message ${fqnString(true)}"

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (declaration.name.identifier == LocalizationPluginContext.LOCALIZATION_NAMESPACE) return declaration
        if (declaration.isFakeOverride) return declaration

        val backingField = declaration.backingField
        requireNotNull(backingField) { declaration.message("must have a backing field") }

        val initializer = backingField.initializer
        requireNotNull(initializer) { declaration.message("must have an initializer") }

        val call = initializer.expression
        require(call is IrCall && call.symbol.owner.name.identifier == "static") { declaration.message("must be a call to 'static'") }

        call.putValueArgument(PROPERTY_NAME_INDEX, backingField.name.identifier.toIrConst(irBuiltIns.stringType))

        pluginContext.resources += "direct//${declaration.name.identifier}"

        return declaration
    }

}
