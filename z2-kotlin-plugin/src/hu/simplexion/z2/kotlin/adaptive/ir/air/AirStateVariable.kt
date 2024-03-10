package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmStateVariable
import org.jetbrains.kotlin.ir.declarations.IrProperty

class AirStateVariable(
    override val armElement: ArmStateVariable,
    override val irProperty: IrProperty
) : AirProperty {

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitStateVariable(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}