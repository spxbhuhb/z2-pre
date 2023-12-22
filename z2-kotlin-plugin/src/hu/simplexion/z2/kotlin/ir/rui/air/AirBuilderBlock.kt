package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirBuilderBlock2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumBlock
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirBuilderBlock(
    override val rumElement: RumBlock,
    override val irFunction: IrSimpleFunction,
    override val externalPatch: AirFunction,
    override val subBuilders: List<AirBuilder>
) : AirBuilder {

    override fun toIr(parent: ClassBoundIrBuilder) = AirBuilderBlock2Ir(parent, this).toIr()

}