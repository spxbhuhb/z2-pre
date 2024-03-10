/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.rum

import hu.simplexion.z2.kotlin.ir.adaptive.ADAPTIVE_DIRTY
import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.ir.adaptive.rum2air.RumDirtyMask2Air
import org.jetbrains.kotlin.name.Name

class RumDirtyMask(
    val rumClass: RumClass,
    val index: Int
) : RumElement {

    val name = Name.identifier("$ADAPTIVE_DIRTY$index")

    fun toAir(parent: ClassBoundIrBuilder) = RumDirtyMask2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitDirtyMask(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) = Unit

}