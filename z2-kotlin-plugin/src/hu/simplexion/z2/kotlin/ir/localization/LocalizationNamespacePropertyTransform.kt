/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.parentAsClass

class LocalizationNamespacePropertyTransform(
    val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (!declaration.isFakeOverride) return declaration

        check(!declaration.isVar) { "localizationNamespace must be immutable" }

        declaration.isFakeOverride = false
        declaration.origin = IrDeclarationOrigin.DEFINED

        transformFakeGetter(declaration.getter !!)

        return declaration
    }

    fun transformFakeGetter(declaration: IrFunction) {
        check(declaration is IrSimpleFunction)

        declaration.origin = IrDeclarationOrigin.GENERATED_SETTER_GETTER
        declaration.isFakeOverride = false

        declaration.body = DeclarationIrBuilder(pluginContext.irContext, declaration.symbol).irBlockBody {
            + irReturn(
                irString(declaration.parentAsClass.fqNameWhenAvailable?.asString() ?: "")
            )
        }
    }
}
