package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderWhen
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchWhen
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmWhen

class ArmWhen2Air(
    parent: ClassBoundIrBuilder,
    val armWhen: ArmWhen
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderWhen = with(armWhen) {

        val externalPatch = AirExternalPatchWhen(
            armWhen,
            externalPatch(irWhen.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderWhen(
            armWhen,
            builder(irWhen.startOffset),
            externalPatch,
            emptyList() // TODO statements.map { it.toAir(this@ArmWhen2Air) }
        )

        airClass.functions += builder

        return builder
    }

}