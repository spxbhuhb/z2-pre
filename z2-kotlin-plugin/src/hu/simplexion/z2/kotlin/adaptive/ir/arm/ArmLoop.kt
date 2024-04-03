/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmLoopBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.BranchBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder

class ArmLoop(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    startOffset : Int,
    var iterator: ArmDeclaration,
    val condition: ArmExpression,
    val loopVariable: ArmDeclaration,
    val body: ArmRenderingStatement,
) : ArmRenderingStatement(armClass, index, closure, startOffset) {

    val target = FqNames.ADAPTIVE_LOOP

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmLoopBuilder(parent, this)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitLoop(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        iterator.accept(visitor, data)
        condition.accept(visitor, data)
        loopVariable.accept(visitor, data)
        body.accept(visitor, data)
    }
}