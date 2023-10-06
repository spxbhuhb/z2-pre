/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsManglerIr.fqnString
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass

class LocalizationProviderTransform(
    val pluginContext: CommonsPluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = pluginContext.irContext.irBuiltIns

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        val type = declaration.backingField?.type ?: irBuiltIns.nothingType

        when {
            declaration.isDelegated -> processDelegated(declaration)
            type.isSubtypeOfClass(pluginContext.localizedText) -> processLocal(declaration, LocalizationResourceType.Text)
            type.isSubtypeOfClass(pluginContext.localizedIcon) -> processLocal(declaration, LocalizationResourceType.Icon)
        }

        return declaration
    }

    fun processDelegated(declaration: IrProperty) {

    }

    fun processLocal(declaration: IrProperty, type: LocalizationResourceType) {
        if (declaration.backingField == null) {
            processGetter(declaration)
        } else {
            processValue(declaration)
        }
    }

    fun processGetter(declaration: IrProperty) {

    }

    /**
     * Put the name of the property into the last parameter of the `static` call.
     */
    fun processValue(declaration: IrProperty) {
        val backingField = declaration.backingField ?: return

        val initializer = backingField.initializer
        requireNotNull(initializer) { "localized field initializer is null: ${declaration.fqnString(true)}" }

        val call = initializer.expression
        if (call !is IrCall) return

        if (call.symbol.owner.name.identifier != "static") return // FIXME check the static function properly
        call.putValueArgument(1, backingField.name.identifier.toIrConst(irBuiltIns.stringType))
    }


}
