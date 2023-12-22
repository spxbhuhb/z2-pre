package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.rum.RumWhen
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirBuilderWhen(
    override val rumElement: RumWhen,
    override val irFunction: IrSimpleFunction,
    override val externalPatch: AirFunction,
    override val subBuilders: List<AirBuilderBlock>
) : AirBuilder {

    override fun toIr(parent: ClassBoundIrBuilder) = TODO()

}