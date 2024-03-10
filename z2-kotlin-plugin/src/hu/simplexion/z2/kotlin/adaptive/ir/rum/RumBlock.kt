/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.rum

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.rum2air.RumBlock2Air
import org.jetbrains.kotlin.ir.expressions.IrBlock

class RumBlock(
    rumClass: RumClass,
    index: Int,
    val irBlock: IrBlock
) : RumRenderingStatement(rumClass, index) {

    val statements = mutableListOf<RumRenderingStatement>()

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.context.adaptiveSymbolMap.getSymbolMap(FqNames.ADAPTIVE_BLOCK_CLASS)

    override fun toAir(parent: ClassBoundIrBuilder) = RumBlock2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitBlock(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {
        statements.forEach { it.accept(visitor, data) }
    }
}