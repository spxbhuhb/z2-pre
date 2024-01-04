package hu.simplexion.z2.kotlin.ir.rui.rum2air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilderForLoop
import hu.simplexion.z2.kotlin.ir.rui.air.AirExternalPatchForLoop
import hu.simplexion.z2.kotlin.ir.rui.rum.RumForLoop

class RumForLoop2Air(
    parent: ClassBoundIrBuilder,
    val rumForLoop: RumForLoop
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderForLoop = with(rumForLoop) {

        val externalPatch = AirExternalPatchForLoop(
            rumForLoop,
            externalPatch(irBlock.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderForLoop(
            rumForLoop,
            builder(irBlock.startOffset),
            externalPatch,
            emptyList() // TODO statements.map { it.toAir(this@RumWhen2Air) }
        )

        airClass.functions += builder

        return builder
    }

}