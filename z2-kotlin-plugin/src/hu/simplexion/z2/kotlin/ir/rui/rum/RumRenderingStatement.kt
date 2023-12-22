/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.rum

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.RuiClassSymbols
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilder

abstract class RumRenderingStatement(
    val rumClass: RumClass,
    val index: Int,
) : RumElement {

    abstract val name: String

    abstract fun symbolMap(irBuilder: ClassBoundIrBuilder): RuiClassSymbols

    abstract fun toAir(parent: ClassBoundIrBuilder): AirBuilder

}