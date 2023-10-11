/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.lower.serialization.ir.JsManglerIr.fqnString
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.parentAsClass

class ClassTransform(
    val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {

    fun IrProperty.message(message : String) = "localization class property $message ${fqnString(true)}"

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (transformNamespace(declaration, pluginContext)) return declaration
        if (declaration.isFakeOverride) return declaration
        if (declaration.hasAnnotation(pluginContext.nonLocalizedAnnotation)) return declaration

        pluginContext.resources += "${declaration.parentAsClass.fqNameWhenAvailable}/${declaration.name.identifier}\t${declaration.name}"

        return declaration
    }

}
