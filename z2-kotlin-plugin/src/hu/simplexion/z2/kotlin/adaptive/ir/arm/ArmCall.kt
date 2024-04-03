/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmCallBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.BranchBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.ir.expressions.IrCall

open class ArmCall(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    val irCall: IrCall,
) : ArmRenderingStatement(armClass, index, closure, irCall.startOffset) {

    val target = irCall.symbol.owner.adaptiveClassFqName()

    val arguments = mutableListOf<ArmValueArgument>()

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmCallBuilder(parent, this)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitCall(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        arguments.forEach { it.accept(visitor, data) }
    }

}