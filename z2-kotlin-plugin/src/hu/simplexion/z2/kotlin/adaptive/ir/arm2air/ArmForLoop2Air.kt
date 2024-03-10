package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderForLoop
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchForLoop
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmForLoop

class ArmForLoop2Air(
    parent: ClassBoundIrBuilder,
    val armForLoop: ArmForLoop
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderForLoop = with(armForLoop) {

        val externalPatch = AirExternalPatchForLoop(
            armForLoop,
            externalPatch(irBlock.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderForLoop(
            armForLoop,
            builder(irBlock.startOffset),
            externalPatch,
            emptyList() // TODO statements.map { it.toAir(this@ArmWhen2Air) }
        )

        airClass.functions += builder

        return builder
    }

}