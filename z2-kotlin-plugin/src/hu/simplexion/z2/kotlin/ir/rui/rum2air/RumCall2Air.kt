package hu.simplexion.z2.kotlin.ir.rui.rum2air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilderCall
import hu.simplexion.z2.kotlin.ir.rui.air.AirExternalPatchCall
import hu.simplexion.z2.kotlin.ir.rui.rum.RumCall

class RumCall2Air(
    parent: ClassBoundIrBuilder,
    val rumCall: RumCall
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderCall = with(rumCall) {

        val externalPatch = AirExternalPatchCall(
            rumCall,
            externalPatch(irCall.startOffset)
        )
        airClass.functions += externalPatch

        val builder = AirBuilderCall(
            rumCall,
            builder(irCall.startOffset),
            externalPatch,
            emptyList()
        )
        airClass.functions += builder

        return builder
    }

}
