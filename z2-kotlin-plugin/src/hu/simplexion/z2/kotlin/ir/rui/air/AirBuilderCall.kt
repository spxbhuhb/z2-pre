package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirBuilderCall2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumCall
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirBuilderCall(
    override val rumElement: RumCall,
    override val irFunction: IrSimpleFunction,
    override val externalPatch: AirFunction,
    override val subBuilders: List<AirBuilderBlock>
) : AirBuilder {

    override fun toIr(parent: ClassBoundIrBuilder) = AirBuilderCall2Ir(parent, this).toIr()

}