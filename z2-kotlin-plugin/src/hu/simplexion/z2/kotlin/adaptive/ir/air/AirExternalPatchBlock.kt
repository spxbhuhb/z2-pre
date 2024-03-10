package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirExternalPatchBlock2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumBlock
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirExternalPatchBlock(
    override val rumElement: RumBlock,
    override val irFunction: IrSimpleFunction
) : AirExternalPatch {

    override fun toIr(parent: ClassBoundIrBuilder) = AirExternalPatchBlock2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitExternalPatchBlock(this, data)


}