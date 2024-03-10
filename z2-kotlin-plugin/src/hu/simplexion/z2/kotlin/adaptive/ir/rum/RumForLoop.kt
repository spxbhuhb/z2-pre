/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.rum

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.rum2air.RumForLoop2Air
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

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.context.adaptiveSymbolMap.getSymbolMap(FqNames.ADAPTIVE_FOR_LOOP_CLASS)

    override fun toAir(parent: ClassBoundIrBuilder) = RumForLoop2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitForLoop(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {
        iterator.accept(visitor, data)
        condition.accept(visitor, data)
        loopVariable.accept(visitor, data)
        body.accept(visitor, data)
    }
}