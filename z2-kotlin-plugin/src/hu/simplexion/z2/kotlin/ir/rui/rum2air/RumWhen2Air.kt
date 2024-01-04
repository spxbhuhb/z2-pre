package hu.simplexion.z2.kotlin.ir.rui.rum2air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilderWhen
import hu.simplexion.z2.kotlin.ir.rui.air.AirExternalPatchWhen
import hu.simplexion.z2.kotlin.ir.rui.rum.RumWhen

class RumWhen2Air(
    parent: ClassBoundIrBuilder,
    val rumWhen: RumWhen
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderWhen = with(rumWhen) {

        val externalPatch = AirExternalPatchWhen(
            rumWhen,
            externalPatch(irWhen.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderWhen(
            rumWhen,
            builder(irWhen.startOffset),
            externalPatch,
            emptyList() // TODO statements.map { it.toAir(this@RumWhen2Air) }
        )

        airClass.functions += builder

        return builder
    }

}