package hu.simplexion.z2.kotlin.ir.adaptive.air

import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.ir.adaptive.air2ir.AirBuilderBlock2Ir
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumBlock
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirBuilderBlock(
    override val rumElement: RumBlock,
    override val irFunction: IrSimpleFunction,
    override val externalPatch: AirFunction,
    override val subBuilders: List<AirBuilder>
) : AirBuilder {

    override fun toIr(parent: ClassBoundIrBuilder) = AirBuilderBlock2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitBuilderBlock(this, data)

}