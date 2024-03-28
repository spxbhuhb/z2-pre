/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.diagnostics.ErrorsAdaptive.ADAPTIVE_IR_MISSING_FUNCTION_BODY
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

/**
 * - creates a [AirClass] for each original function (a function annotated with `@Adaptive`)
 * - replaces the body of each original function with an empty one
 */
class OriginalFunctionTransform(
    private val adaptiveContext: AdaptivePluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = adaptiveContext.irContext.irBuiltIns

    /**
     * Transforms a function annotated with `@Adaptive` into a Adaptive fragment class.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (!declaration.isAdaptiveFunction()) {
            return super.visitFunctionNew(declaration) as IrFunction
        }

        if (declaration.body == null) {
            ADAPTIVE_IR_MISSING_FUNCTION_BODY.report(adaptiveContext, declaration)
            return super.visitFunctionNew(declaration) as IrFunction
        }

        IrFunction2ArmClass(adaptiveContext, declaration, 0).transform()

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
    }

    fun IrFunction.isAdaptiveFunction(): Boolean =
        // FIXME recognition of adaptive function calls
        (extensionReceiverParameter?.let { it.type == adaptiveContext.adaptiveNamespaceClass.defaultType } ?: false)


}
