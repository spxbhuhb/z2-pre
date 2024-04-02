package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirEntryPoint2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmEntryPoint

class AirEntryPoint(
    val armElement: ArmEntryPoint,
    val airClass: AirClass
) : AirElement {

    val armEntryPoint
        get() = armElement

    fun toIr(context: AdaptivePluginContext) = AirEntryPoint2Ir(context, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitEntryPoint(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}