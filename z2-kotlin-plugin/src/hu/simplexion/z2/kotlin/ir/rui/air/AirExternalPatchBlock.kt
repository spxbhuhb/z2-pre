package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirExternalPatchBlock2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumBlock
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirExternalPatchBlock(
    override val rumElement: RumBlock,
    override val irFunction: IrSimpleFunction
) : AirFunction {

    override fun toIr(parent: ClassBoundIrBuilder) = AirExternalPatchBlock2Ir(parent, this).toIr()

}