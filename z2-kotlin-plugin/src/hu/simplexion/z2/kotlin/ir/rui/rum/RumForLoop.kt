/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.rum

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.RUI_FOR_LOOP
import hu.simplexion.z2.kotlin.ir.rui.RUI_FQN_FOR_LOOP_CLASS
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilderBlock
import hu.simplexion.z2.kotlin.ir.rui.rum.visitors.RumElementVisitor
import org.jetbrains.kotlin.ir.expressions.IrBlock

class RumForLoop(
    rumClass: RumClass,
    index: Int,
    val irBlock: IrBlock,
    var iterator: RumDeclaration,
    val condition: RumExpression,
    val loopVariable: RumDeclaration,
    val body: RumRenderingStatement,
) : RumRenderingStatement(rumClass, index) {

    override val name = "$RUI_FOR_LOOP$index"

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.context.ruiSymbolMap.getSymbolMap(RUI_FQN_FOR_LOOP_CLASS)

    override fun toAir(parent: ClassBoundIrBuilder): AirBuilderBlock {
        TODO("Not yet implemented")
    }

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitForLoop(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {
        iterator.accept(visitor, data)
        condition.accept(visitor, data)
        loopVariable.accept(visitor, data)
        body.accept(visitor, data)
    }
}