package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirExternalPatchForLoop2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumForLoop
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirExternalPatchForLoop(
    override val rumElement: RumForLoop,
    override val irFunction: IrSimpleFunction
) : AirExternalPatch {

    override fun toIr(parent: ClassBoundIrBuilder) = AirExternalPatchForLoop2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitExternalPatchForLoop(this, data)

}