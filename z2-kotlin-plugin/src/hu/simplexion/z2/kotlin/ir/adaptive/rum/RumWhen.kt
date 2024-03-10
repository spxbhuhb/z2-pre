/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.rum

import hu.simplexion.z2.kotlin.ir.adaptive.ADAPTIVE_FQN_WHEN_CLASS
import hu.simplexion.z2.kotlin.ir.adaptive.ADAPTIVE_WHEN
import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.ir.adaptive.rum2air.RumWhen2Air
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrWhen

class RumWhen(
    rumClass: RumClass,
    index: Int,
    val irSubject: IrVariable?,
    val irWhen: IrWhen
) : RumRenderingStatement(rumClass, index) {

    override val name = "$ADAPTIVE_WHEN$index"

    val branches = mutableListOf<RumBranch>()

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.context.adaptiveSymbolMap.getSymbolMap(ADAPTIVE_FQN_WHEN_CLASS)

    override fun toAir(parent: ClassBoundIrBuilder) = RumWhen2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitWhen(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {
        branches.forEach { it.accept(visitor, data) }
    }
}