package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirExternalPatchSequence2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSequence
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirExternalPatchSequence(
    override val armElement: ArmSequence,
    override val irFunction: IrSimpleFunction
) : AirExternalPatch {

    override fun toIr(parent: ClassBoundIrBuilder) = AirExternalPatchSequence2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitExternalPatchSequence(this, data)


}