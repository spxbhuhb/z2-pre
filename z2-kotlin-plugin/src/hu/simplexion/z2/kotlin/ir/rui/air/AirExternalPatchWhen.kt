package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirExternalPatchWhen2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumWhen
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirExternalPatchWhen(
    override val rumElement: RumWhen,
    override val irFunction: IrSimpleFunction
) : AirExternalPatch {

    override fun toIr(parent: ClassBoundIrBuilder) = AirExternalPatchWhen2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitExternalPatchWhen(this, data)

}