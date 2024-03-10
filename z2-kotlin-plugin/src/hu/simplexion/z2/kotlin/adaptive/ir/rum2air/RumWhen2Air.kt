package hu.simplexion.z2.kotlin.adaptive.ir.rum2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderWhen
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchWhen
import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumWhen

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