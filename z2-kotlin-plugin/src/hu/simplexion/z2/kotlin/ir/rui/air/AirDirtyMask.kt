package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.rum.RumDirtyMask
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirDirtyMask(
    override val rumElement: RumDirtyMask,
    override val irProperty: IrProperty,
    val invalidate: IrSimpleFunction
) : AirProperty