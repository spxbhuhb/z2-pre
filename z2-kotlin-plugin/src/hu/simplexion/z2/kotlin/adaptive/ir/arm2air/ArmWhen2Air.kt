package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderWhen
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchWhen
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirFragmentFactory
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

        val subBuilders = branches.map { it.result.toAir(this@ArmWhen2Air) }

        val fragmentFactory = AirFragmentFactory(
            armWhen,
            fragmentFactory(irWhen.startOffset),
            subBuilders
        )
        airClass.functions += fragmentFactory

        val builder = AirBuilderWhen(
            armWhen,
            builder(irWhen.startOffset),
            externalPatch,
            subBuilders,
            fragmentFactory
        )
        airClass.functions += builder

        return builder
    }

}