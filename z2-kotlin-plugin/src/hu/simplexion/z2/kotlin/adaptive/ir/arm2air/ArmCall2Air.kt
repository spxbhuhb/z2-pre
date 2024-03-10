package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderCall
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchCall
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmCall

class ArmCall2Air(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderCall = with(armCall) {

        val externalPatch = AirExternalPatchCall(
            armCall,
            externalPatch(irCall.startOffset)
        )
        airClass.functions += externalPatch

        val builder = AirBuilderCall(
            armCall,
            builder(irCall.startOffset),
            externalPatch,
            emptyList()
        )
        airClass.functions += builder

        return builder
    }

}
