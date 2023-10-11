/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrEnumEntry
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.parentAsClass

class EnumTransform(
    val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        transformNamespace(declaration, pluginContext)
        return declaration
    }
    override fun visitEnumEntry(declaration: IrEnumEntry): IrStatement {
        pluginContext.resources += "${declaration.parentAsClass.fqNameWhenAvailable}/${declaration.name}\t${declaration.name}"
        return super.visitEnumEntry(declaration)
    }

}
