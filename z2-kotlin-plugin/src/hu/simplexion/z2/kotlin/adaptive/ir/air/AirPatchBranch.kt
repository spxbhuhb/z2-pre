package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import org.jetbrains.kotlin.ir.expressions.IrBlock

class AirPatchBranch(
    val index : Int,
    val block : IrBlock
) : AirElement {

//    override fun toIr(parent: ClassBoundIrBuilder) = AirBuilderCall2Ir(parent, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitPatchBranch(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) {

    }
}