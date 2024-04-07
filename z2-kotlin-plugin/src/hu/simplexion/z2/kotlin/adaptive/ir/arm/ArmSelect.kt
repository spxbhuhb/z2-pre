/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmSelectBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.BranchBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.symbols.IrSymbol

class ArmSelect(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    startOffset: Int,
    val subjectSymbol : IrSymbol?,
    val subjectExpression: ArmExpression?
) : ArmRenderingStatement(armClass, index, closure, startOffset) {

    val target = FqNames.ADAPTIVE_SELECT

    val branches = mutableListOf<ArmBranch>()

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmSelectBuilder(parent, this)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitWhen(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        branches.forEach { it.accept(visitor, data) }
    }
}