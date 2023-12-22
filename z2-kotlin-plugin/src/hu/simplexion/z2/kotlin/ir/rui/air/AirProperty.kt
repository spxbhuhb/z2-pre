package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.rum.RumElement
import org.jetbrains.kotlin.ir.declarations.IrProperty

interface AirProperty : AirElement {
    override val rumElement: RumElement
    val irProperty: IrProperty
}