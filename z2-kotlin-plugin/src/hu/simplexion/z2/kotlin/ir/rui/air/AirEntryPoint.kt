package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirEntryPoint2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumEntryPoint

class AirEntryPoint(
    override val rumElement: RumEntryPoint,
    val airClass: AirClass
) : AirElement {

    val rumEntryPoint
        get() = rumElement

    fun toIr(context: RuiPluginContext) = AirEntryPoint2Ir(context, this).toIr()

}