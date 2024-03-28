/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import org.jetbrains.kotlin.ir.expressions.IrExpression

/**
 * A function argument that is a function itself, but not an adaptive one.
 */
class ArmSupportFunctionArgument(
    armClass: ArmClass,
    index: Int,
    value: IrExpression,
    dependencies: ArmDependencies,
) : ArmValueArgument(armClass, index, value, dependencies) {

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitSupportFunctionArgument(this, data)

}