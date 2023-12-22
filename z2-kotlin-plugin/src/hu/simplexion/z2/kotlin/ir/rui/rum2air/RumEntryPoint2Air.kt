package hu.simplexion.z2.kotlin.ir.rui.rum2air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.air.AirEntryPoint
import hu.simplexion.z2.kotlin.ir.rui.rum.RumEntryPoint

class RumEntryPoint2Air(
    context: RuiPluginContext,
    val entryPoint: RumEntryPoint
) : ClassBoundIrBuilder(context) {

    fun toAir(): AirEntryPoint {
        return AirEntryPoint(
            entryPoint,
            context.airClasses[entryPoint.rumClass.fqName]!!
        )
    }
}
