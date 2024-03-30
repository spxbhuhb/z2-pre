/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import org.jetbrains.kotlin.ir.expressions.IrExpression

class ArmFragmentFactoryArgument(
    armClass: ArmClass,
    index: Int,
    value: IrExpression,
    dependencies: ArmDependencies
) : ArmValueArgument(armClass, index, value, dependencies) {

    lateinit var closure: ArmState

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitFragmentFactoryArgument(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

}