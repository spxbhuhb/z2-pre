package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderSequence
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchSequence
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSequence

class ArmSequence2Air(
    parent: ClassBoundIrBuilder,
    val armSequence: ArmSequence
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderSequence = with(armSequence) {

        val externalPatch = AirExternalPatchSequence(
            armSequence,
            externalPatch(irBlock.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderSequence(
            armSequence,
            builder(irBlock.startOffset),
            externalPatch,
            statements.map { it.toAir(this@ArmSequence2Air) }
        )

        airClass.functions += builder

        return builder
    }

}