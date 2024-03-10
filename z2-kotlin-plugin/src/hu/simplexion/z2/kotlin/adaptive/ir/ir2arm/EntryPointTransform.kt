/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.diagnostics.ErrorsAdaptive
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmEntryPoint
import hu.simplexion.z2.kotlin.adaptive.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.superTypes
import org.jetbrains.kotlin.psi.KtModifierListOwner

/**
 * Creates a [ArmClass] and a [ArmEntryPoint] for each call of the `adaptive` function (defined in the runtime).
 */
class EntryPointTransform(
    private val adaptiveContext: AdaptivePluginContext
) : IrElementTransformerVoidWithContext(), AdaptiveAnnotationBasedExtension {

    val irBuiltIns = adaptiveContext.irContext.irBuiltIns

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        listOf(Strings.ADAPTIVE_ANNOTATION)

    /**
     * Transforms Adaptive entry points (calls to the function `adaptive`) into a
     * Adaptive root class and modifies the last parameter of the function (which
     * has to be a lambda) so it gets an adapter and creates a new instance
     * of the root class.
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol.owner.kotlinFqName != FqNames.ADAPTIVE_ENTRY_FUNCTION) {
            return super.visitCall(expression)
        }

        fun report(message: String): IrExpression {
            ErrorsAdaptive.ADAPTIVE_IR_INTERNAL_PLUGIN_ERROR.report(adaptiveContext, currentFile.fileEntry, expression.startOffset, message)
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
            return report("${expression.symbol} does not have AdaptiveAdapter as first parameter")
        }

        val adapter = function.valueParameters.first()

        val classifier = adaptiveContext.adaptiveAdapterType.classifierOrNull

        if (adapter.type.classifierOrNull != classifier) {
            if (adapter.type.superTypes().firstOrNull { it.classifierOrNull == classifier } == null) {
                return report("${expression.symbol} first parameter is not a AdaptiveAdapter")
            }
        }

        // skip the adaptiveAdapter function parameter
        val armClass = IrFunction2Arm(adaptiveContext, function, skipParameters = 1).transform()

        ArmEntryPoint(armClass, function).also {
            adaptiveContext.armEntryPoints += it
        }

        return expression
    }

}
