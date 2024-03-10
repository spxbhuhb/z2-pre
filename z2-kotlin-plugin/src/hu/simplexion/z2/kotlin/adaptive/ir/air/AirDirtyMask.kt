package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmDirtyMask
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirDirtyMask(
    override val armElement: ArmDirtyMask,
    override val irProperty: IrProperty,
    val invalidate: IrSimpleFunction
) : AirProperty {

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitDirtyMask(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}