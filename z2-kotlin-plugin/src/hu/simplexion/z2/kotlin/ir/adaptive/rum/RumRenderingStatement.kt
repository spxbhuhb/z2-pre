/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.rum

import hu.simplexion.z2.kotlin.ir.adaptive.AdaptiveClassSymbols
import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirBuilder

abstract class RumRenderingStatement(
    val rumClass: RumClass,
    val index: Int,
) : RumElement {

    abstract val name: String

    abstract fun symbolMap(irBuilder: ClassBoundIrBuilder): AdaptiveClassSymbols

    abstract fun toAir(parent: ClassBoundIrBuilder): AirBuilder

}