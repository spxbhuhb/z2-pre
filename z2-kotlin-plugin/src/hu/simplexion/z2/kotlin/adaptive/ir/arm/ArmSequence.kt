/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmSequence2Air
import org.jetbrains.kotlin.ir.expressions.IrBlock

class ArmSequence(
    armClass: ArmClass,
    index: Int,
    val irBlock: IrBlock
) : ArmRenderingStatement(armClass, index) {

    val statements = mutableListOf<ArmRenderingStatement>()

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.context.adaptiveSymbolMap.getSymbolMap(FqNames.ADAPTIVE_SEQUENCE_CLASS)

    override fun toAir(parent: ClassBoundIrBuilder) = ArmSequence2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitSequence(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        statements.forEach { it.accept(visitor, data) }
    }
}