/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmSupportFunctionArgumentBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression

/**
 * A function argument that is a function itself, but not an adaptive one.
 */
class ArmSupportFunctionArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    val supportFunctionIndex: Int,
    value: IrFunctionExpression,
    dependencies: ArmDependencies,
) : ArmValueArgument(armClass, argumentIndex, value, dependencies, ArmExpressionOrigin.SUPPORT_FUNCTION_ARGUMENT) {

    override fun toPatchExpression(classBuilder: ClassBoundIrBuilder, patchFun : IrSimpleFunction, closure: ArmClosure, fragmentParameter: IrValueParameter, closureDirtyMask: IrVariable) =
        ArmSupportFunctionArgumentBuilder(classBuilder, this, closure, fragmentParameter, closureDirtyMask).genPatchDescendantExpression(patchFun)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitSupportFunctionArgument(this, data)

}