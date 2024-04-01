package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import org.jetbrains.kotlin.ir.expressions.IrExpression

class AirInvokeBranch(
    val index : Int,
    val body : IrExpression
) : AirElement {

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitInvokeBranch(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) {

    }
}