package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.rum.RumDirtyMask
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

class AirDirtyMask(
    override val rumElement: RumDirtyMask,
    override val irProperty: IrProperty,
    val invalidate: IrSimpleFunction
) : AirProperty {

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitDirtyMask(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}