/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrEnumEntry
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.parentAsClass

class EnumTransform(
    val pluginContext: LocalizationPluginContext
) : IrElementTransformerVoidWithContext() {
    override fun visitEnumEntry(declaration: IrEnumEntry): IrStatement {
        pluginContext.resources += "enum/${declaration.parentAsClass.fqNameWhenAvailable}/${declaration.name}"
        return super.visitEnumEntry(declaration)
    }
}
