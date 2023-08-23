/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.access

import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import hu.simplexion.z2.schematic.kotlin.ir.util.IrBuilder
import hu.simplexion.z2.schematic.kotlin.ir.util.SchematicFunctionType
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression

class SchematicAccessTransform(
    override val pluginContext: SchematicPluginContext
) : IrElementTransformerVoidWithContext(), IrBuilder {

    override fun visitCall(expression: IrCall): IrExpression {
        val functionType = pluginContext.funCache[expression]
        if (functionType != SchematicFunctionType.SchematicAccess) {
            return super.visitCall(expression)
        }

        return SchematicAccessCallTransform(pluginContext, expression).transform()
    }

}
