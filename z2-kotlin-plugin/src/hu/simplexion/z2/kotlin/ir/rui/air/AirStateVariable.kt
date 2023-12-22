package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.rum.RumStateVariable
import org.jetbrains.kotlin.ir.declarations.IrProperty

class AirStateVariable(
    override val rumElement: RumStateVariable,
    override val irProperty: IrProperty
) : AirProperty