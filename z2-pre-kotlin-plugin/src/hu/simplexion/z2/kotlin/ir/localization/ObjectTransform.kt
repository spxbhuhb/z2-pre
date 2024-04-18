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
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.parentAsClass

class ObjectTransform(
    val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = pluginContext.irContext.irBuiltIns

    fun IrProperty.message(message: String) = "localization object property $message ${fqnString(true)}"

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        when {
            transformNamespace(declaration, pluginContext) -> Unit
            declaration.isFakeOverride -> inherited(declaration)
            else -> direct(declaration)
        }

        return declaration
    }

    fun inherited(declaration: IrProperty) {
        pluginContext.resources += "${declaration.parentAsClass.fqNameWhenAvailable}/${declaration.name.identifier}\t"
    }

    fun direct(declaration: IrProperty) {
        val backingField = declaration.backingField
        requireNotNull(backingField) { declaration.message("must have a backing field") }

        val initializer = backingField.initializer
        requireNotNull(initializer) { declaration.message("must have an initializer") }

        val call = initializer.expression
        require(call is IrCall && call.symbol.owner.name.identifier == "static") { declaration.message("must be a call to 'static'") }

        call.putValueArgument(STATIC_NAME_ARG_INDEX, backingField.name.identifier.toIrConst(irBuiltIns.stringType))

        pluginContext.register(declaration, declaration.name, call)
    }

}
