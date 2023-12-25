/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.ir2rum

import hu.simplexion.z2.kotlin.ir.rui.RUI_FQN_ENTRY_FUNCTION
import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.diagnostics.ErrorsRui
import hu.simplexion.z2.kotlin.ir.rui.rum.RumClass
import hu.simplexion.z2.kotlin.ir.rui.rum.RumEntryPoint
import hu.simplexion.z2.kotlin.ir.rui.util.RuiAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.superTypes
import org.jetbrains.kotlin.psi.KtModifierListOwner

/**
 * Creates a [RumClass] and a [RumEntryPoint] for each call of the `rui` function (defined in the runtime).
 */
class EntryPointTransform(
    private val ruiContext: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    val irBuiltIns = ruiContext.irContext.irBuiltIns

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    /**
     * Transforms Rui entry points (calls to the function `rui`) into a
     * Rui root class and modifies the last parameter of the function (which
     * has to be a lambda) so it gets an adapter and creates a new instance
     * of the root class.
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol.owner.kotlinFqName != RUI_FQN_ENTRY_FUNCTION) {
            return super.visitCall(expression)
        }

        fun report(message: String): IrExpression {
            ErrorsRui.RUI_IR_INTERNAL_PLUGIN_ERROR.report(ruiContext, currentFile.fileEntry, expression.startOffset, message)
            return super.visitCall(expression)
        }

        if (expression.valueArgumentsCount == 0) {
            return report("${expression.symbol} value arguments count == 0")
        }

        val block = expression.getValueArgument(expression.valueArgumentsCount - 1)

        if (block !is IrFunctionExpression) {
            return report("${expression.symbol} last parameter is not a function expression")
        }

        val function = block.function

        if (function.valueParameters.isEmpty()) {
            return report("${expression.symbol} does not have RuiAdapter as first parameter")
        }

        val adapter = function.valueParameters.first()

        val classifier = ruiContext.ruiAdapterType.classifierOrNull

        if (adapter.type.classifierOrNull != classifier) {
            if (adapter.type.superTypes().firstOrNull { it.classifierOrNull == classifier } == null) {
                return report("${expression.symbol} first parameter is not a RuiAdapter")
            }
        }

        // skip the ruiAdapter function parameter
        val rumClass = IrFunction2Rum(ruiContext, function, skipParameters = 1).transform()

        RumEntryPoint(rumClass, function).also {
            ruiContext.rumEntryPoints += it
        }

        return expression
    }

}
