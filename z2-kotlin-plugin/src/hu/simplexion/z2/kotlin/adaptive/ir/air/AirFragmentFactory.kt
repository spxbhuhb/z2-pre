package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirFragmentFactory2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmWhen
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirFragmentFactory(
    override val armElement: ArmWhen,
    override val irFunction: IrSimpleFunction,
    val subBuilders: List<AirBuilder>
) : AirFunction {

    override fun toIr(parent: ClassBoundIrBuilder) = AirFragmentFactory2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitFragmentFactory(this, data)

}