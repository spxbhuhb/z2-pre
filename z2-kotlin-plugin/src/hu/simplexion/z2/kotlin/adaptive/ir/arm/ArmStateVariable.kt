/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirStateVariable
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.name.Name

interface ArmStateVariable : ArmElement {

    val armClass: ArmClass
    val index: Int
    val originalName: String
    val name: Name

    fun matches(symbol: IrSymbol): Boolean

    fun toAir(parent: ClassBoundIrBuilder): AirStateVariable

}