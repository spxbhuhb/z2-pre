/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.rum

import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.air.AirEntryPoint
import hu.simplexion.z2.kotlin.ir.rui.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.rum2air.RumEntryPoint2Air
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class RumEntryPoint(
    val rumClass: RumClass,
    val irFunction: IrSimpleFunction,
) : RumElement {

    fun toAir(context: RuiPluginContext): AirEntryPoint = RumEntryPoint2Air(context, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitEntryPoint(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {

    }
}