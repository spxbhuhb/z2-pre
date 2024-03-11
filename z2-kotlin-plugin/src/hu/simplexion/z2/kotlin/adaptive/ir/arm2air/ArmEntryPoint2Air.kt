package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirEntryPoint
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmEntryPoint

class ArmEntryPoint2Air(
    context: AdaptivePluginContext,
    val entryPoint: ArmEntryPoint
) : ClassBoundIrBuilder(context) {

    fun toAir(): AirEntryPoint {
        return AirEntryPoint(
            entryPoint,
            pluginContext.airClasses[entryPoint.armClass.fqName]!!
        )
    }
}
