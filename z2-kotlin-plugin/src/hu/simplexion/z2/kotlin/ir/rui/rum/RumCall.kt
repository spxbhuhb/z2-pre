/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.rum

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.RUI_CALL
import hu.simplexion.z2.kotlin.ir.rui.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.rum2air.RumCall2Air
import hu.simplexion.z2.kotlin.ir.rui.toRuiClassFqName
import org.jetbrains.kotlin.ir.expressions.IrCall

open class RumCall(
    rumClass: RumClass,
    index: Int,
    val irCall: IrCall
) : RumRenderingStatement(rumClass, index) {

    override val name = "$RUI_CALL$index"

    val target = irCall.symbol.owner.toRuiClassFqName(rumClass.ruiContext)

    val valueArguments = mutableListOf<RumExpression>()

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.context.ruiSymbolMap.getSymbolMap(target)

    override fun toAir(parent: ClassBoundIrBuilder) = RumCall2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitCall(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {
        valueArguments.forEach { it.accept(visitor, data) }
    }
}