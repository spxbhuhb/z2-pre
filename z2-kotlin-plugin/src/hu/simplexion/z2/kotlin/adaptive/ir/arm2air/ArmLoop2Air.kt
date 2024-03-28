package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderForLoop
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchForLoop
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmLoop

class ArmLoop2Air(
    parent: ClassBoundIrBuilder,
    val armLoop: ArmLoop
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderForLoop = with(armLoop) {

        val externalPatch = AirExternalPatchForLoop(
            armLoop,
            externalPatch(irBlock.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderForLoop(
            armLoop,
            builder(irBlock.startOffset),
            externalPatch,
            emptyList() // TODO statements.map { it.toAir(this@ArmSelect2Air) }
        )

        airClass.functions += builder

        return builder
    }

}