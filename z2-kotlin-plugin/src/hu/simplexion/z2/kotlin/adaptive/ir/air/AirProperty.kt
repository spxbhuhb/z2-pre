package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumElement
import org.jetbrains.kotlin.ir.declarations.IrProperty

interface AirProperty : AirElement {
    override val rumElement: RumElement
    val irProperty: IrProperty
}