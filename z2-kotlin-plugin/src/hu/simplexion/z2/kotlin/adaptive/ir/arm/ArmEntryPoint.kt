/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirEntryPoint
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmEntryPoint2Air
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class ArmEntryPoint(
    val armClass: ArmClass,
    val irFunction: IrSimpleFunction,
) : ArmElement {

    fun toAir(context: AdaptivePluginContext): AirEntryPoint = ArmEntryPoint2Air(context, this).toAir()

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitEntryPoint(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {

    }
}