package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import org.jetbrains.kotlin.ir.expressions.IrExpression

class AirBuildBranch(
    val index : Int,
    val expression : IrExpression
) : AirElement {

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitBuildBranch(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) {

    }
}