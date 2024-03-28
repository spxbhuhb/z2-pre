/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirStateVariable
import org.jetbrains.kotlin.ir.symbols.IrSymbol

interface ArmStateVariable : ArmElement {

    val armClass: ArmClass
    val indexInState: Int
    val indexInClosure: Int
    val name: String

    fun matches(symbol: IrSymbol): Boolean

    fun toAir(parent: ClassBoundIrBuilder): AirStateVariable

}