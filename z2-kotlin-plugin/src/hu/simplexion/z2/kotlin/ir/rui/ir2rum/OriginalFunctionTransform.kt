/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.ir2rum

import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.diagnostics.ErrorsRui.RUI_IR_MISSING_FUNCTION_BODY
import hu.simplexion.z2.kotlin.ir.rui.rum.RumClass
import hu.simplexion.z2.kotlin.ir.rui.util.RuiAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.psi.KtModifierListOwner

/**
 * - creates a [RumClass] for each original function (a function annotated with `@Rui`)
 * - replaces the body of each original function with an empty one
 */
class OriginalFunctionTransform(
    private val ruiContext: RuiPluginContext
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    val irBuiltIns = ruiContext.irContext.irBuiltIns

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    /**
     * Transforms a function annotated with `@Rui` into a Rui fragment class.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (!declaration.isAnnotatedWithRui()) {
            return super.visitFunctionNew(declaration) as IrFunction
        }

        if (declaration.body == null) {
            RUI_IR_MISSING_FUNCTION_BODY.report(ruiContext, declaration)
            return super.visitFunctionNew(declaration) as IrFunction
        }

        IrFunction2Rum(ruiContext, declaration, 0).transform()

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
    }

}
