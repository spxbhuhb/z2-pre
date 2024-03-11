/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmForLoop2Air
import org.jetbrains.kotlin.ir.expressions.IrBlock

class ArmForLoop(
    armClass: ArmClass,
    index: Int,
    val irBlock: IrBlock,
    var iterator: ArmDeclaration,
    val condition: ArmExpression,
    val loopVariable: ArmDeclaration,
    val body: ArmRenderingStatement,
) : ArmRenderingStatement(armClass, index) {

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.pluginContext.adaptiveSymbolMap.getSymbolMap(FqNames.ADAPTIVE_FOR_LOOP_CLASS)

    override fun toAir(parent: ClassBoundIrBuilder) = ArmForLoop2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitForLoop(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        iterator.accept(visitor, data)
        condition.accept(visitor, data)
        loopVariable.accept(visitor, data)
        body.accept(visitor, data)
    }
}