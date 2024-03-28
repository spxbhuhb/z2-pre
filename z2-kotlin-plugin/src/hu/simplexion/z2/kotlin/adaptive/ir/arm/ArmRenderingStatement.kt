/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptiveClassSymbols
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder

abstract class ArmRenderingStatement(
    val armClass: ArmClass,
    open val index: Int,
) : ArmElement {

    abstract fun symbolMap(irBuilder: ClassBoundIrBuilder): AdaptiveClassSymbols

    abstract fun toAir(parent: ClassBoundIrBuilder)

}