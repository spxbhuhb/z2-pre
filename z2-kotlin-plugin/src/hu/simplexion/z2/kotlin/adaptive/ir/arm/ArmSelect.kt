/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmSelect2Air
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrWhen

class ArmSelect(
    armClass: ArmClass,
    index: Int,
    val irSubject: IrVariable?,
    val irWhen: IrWhen
) : ArmRenderingStatement(armClass, index) {

    val branches = mutableListOf<ArmBranch>()

    override fun symbolMap(irBuilder: ClassBoundIrBuilder) =
        irBuilder.pluginContext.adaptiveSymbolMap.getSymbolMap(FqNames.ADAPTIVE_SELECT)

    override fun toAir(parent: ClassBoundIrBuilder) = ArmSelect2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitWhen(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        branches.forEach { it.accept(visitor, data) }
    }
}