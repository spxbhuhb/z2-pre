/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmCall2Air
import hu.simplexion.z2.kotlin.adaptive.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.ir.expressions.IrCall

open class ArmCall(
    armClass: ArmClass,
    index: Int,
    val irCall: IrCall
) : ArmRenderingStatement(armClass, index) {

    open val target = irCall.symbol.owner.adaptiveClassFqName()

    val arguments = mutableListOf<ArmValueArgument>()

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) = irBuilder.pluginContext.adaptiveSymbolMap.getSymbolMap(target)

    override fun toAir(parent: ClassBoundIrBuilder) {
        ArmCall2Air(parent, this).toAir()
    }

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitCall(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        arguments.forEach { it.accept(visitor, data) }
    }

}