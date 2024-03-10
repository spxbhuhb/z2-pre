package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirEntryPoint2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumEntryPoint

class AirEntryPoint(
    override val rumElement: RumEntryPoint,
    val airClass: AirClass
) : AirElement {

    val rumEntryPoint
        get() = rumElement

    fun toIr(context: AdaptivePluginContext) = AirEntryPoint2Ir(context, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitEntryPoint(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}