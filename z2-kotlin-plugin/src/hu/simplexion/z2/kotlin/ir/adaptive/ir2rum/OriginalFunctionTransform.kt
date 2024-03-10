/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.ir2rum

import hu.simplexion.z2.kotlin.ir.adaptive.AdaptivePluginContext
import hu.simplexion.z2.kotlin.ir.adaptive.diagnostics.ErrorsAdaptive.ADAPTIVE_IR_MISSING_FUNCTION_BODY
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumClass
import hu.simplexion.z2.kotlin.ir.adaptive.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.psi.KtModifierListOwner

/**
 * - creates a [RumClass] for each original function (a function annotated with `@Adaptive`)
 * - replaces the body of each original function with an empty one
 */
class OriginalFunctionTransform(
    private val adaptiveContext: AdaptivePluginContext
) : IrElementTransformerVoidWithContext(), AdaptiveAnnotationBasedExtension {

    val irBuiltIns = adaptiveContext.irContext.irBuiltIns

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        adaptiveContext.annotations

    /**
     * Transforms a function annotated with `@Adaptive` into a Adaptive fragment class.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (!declaration.isAnnotatedWithAdaptive()) {
            return super.visitFunctionNew(declaration) as IrFunction
        }

        if (declaration.body == null) {
            ADAPTIVE_IR_MISSING_FUNCTION_BODY.report(adaptiveContext, declaration)
            return super.visitFunctionNew(declaration) as IrFunction
        }

        IrFunction2Rum(adaptiveContext, declaration, 0).transform()

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
    }

}
