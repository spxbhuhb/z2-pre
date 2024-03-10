package hu.simplexion.z2.kotlin.adaptive.ir.rum2air

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirEntryPoint
import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumEntryPoint

class RumEntryPoint2Air(
    context: AdaptivePluginContext,
    val entryPoint: RumEntryPoint
) : ClassBoundIrBuilder(context) {

    fun toAir(): AirEntryPoint {
        return AirEntryPoint(
            entryPoint,
            context.airClasses[entryPoint.rumClass.fqName]!!
        )
    }
}
