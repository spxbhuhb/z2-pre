package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmElement
import org.jetbrains.kotlin.ir.declarations.IrProperty

interface AirProperty : AirElement {
    override val armElement: ArmElement
    val irProperty: IrProperty
}