package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirExternalPatchCall2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumCall
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirExternalPatchCall(
    override val rumElement: RumCall,
    override val irFunction: IrSimpleFunction
) : AirExternalPatch {

    override fun toIr(parent: ClassBoundIrBuilder) = AirExternalPatchCall2Ir(parent, this).toIr()

}