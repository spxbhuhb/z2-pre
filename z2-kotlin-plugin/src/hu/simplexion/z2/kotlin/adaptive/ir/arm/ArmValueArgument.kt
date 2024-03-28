/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmValueArgument2Air
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl

open class ArmValueArgument(
    armClass: ArmClass,
    val index: Int,
    val value: IrExpression,
    dependencies: ArmDependencies
) : ArmExpression(armClass, value, ArmExpressionOrigin.VALUE_ARGUMENT, dependencies) {

    fun toPatchExpression(classBuilder : ClassBoundIrBuilder, fragmentParameter: IrValueParameter, block: IrBlockImpl) {
        ArmValueArgument2Air(classBuilder, this, fragmentParameter, block).toPatchExpression()
    }

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitValueArgument(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

}

