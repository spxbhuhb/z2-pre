package hu.simplexion.z2.kotlin.ir.adaptive.air

import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumElement
import org.jetbrains.kotlin.ir.declarations.IrProperty

interface AirProperty : AirElement {
    override val rumElement: RumElement
    val irProperty: IrProperty
}