package hu.simplexion.z2.kotlin.ir.adaptive.rum2air

import hu.simplexion.z2.kotlin.ir.adaptive.AdaptivePluginContext
import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirEntryPoint
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumEntryPoint

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
