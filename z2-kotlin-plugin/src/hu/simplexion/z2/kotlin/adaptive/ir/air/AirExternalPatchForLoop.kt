package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirExternalPatchForLoop2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmForLoop
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirExternalPatchForLoop(
    override val armElement: ArmForLoop,
    override val irFunction: IrSimpleFunction
) : AirExternalPatch {

    override fun toIr(parent: ClassBoundIrBuilder) = AirExternalPatchForLoop2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitExternalPatchForLoop(this, data)

}