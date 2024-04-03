/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmValueArgumentBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

open class ArmValueArgument(
    armClass: ArmClass,
    val argumentIndex: Int,
    val value: IrExpression,
    dependencies: ArmDependencies,
    origin: ArmExpressionOrigin = ArmExpressionOrigin.VALUE_ARGUMENT
) : ArmExpression(armClass, value, origin, dependencies) {

    open fun toPatchExpression(classBuilder: ClassBoundIrBuilder, patchFun: IrSimpleFunction, closure: ArmClosure, fragmentParameter: IrValueParameter, closureDirtyMask: IrVariable) =
        ArmValueArgumentBuilder(classBuilder, this, closure, fragmentParameter, closureDirtyMask).genPatchDescendantExpression(patchFun)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitValueArgument(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

}

