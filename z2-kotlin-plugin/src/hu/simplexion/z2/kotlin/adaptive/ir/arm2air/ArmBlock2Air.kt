package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderBlock
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchBlock
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmBlock

class ArmBlock2Air(
    parent: ClassBoundIrBuilder,
    val armBlock: ArmBlock
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderBlock = with(armBlock) {

        val externalPatch = AirExternalPatchBlock(
            armBlock,
            externalPatch(irBlock.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderBlock(
            armBlock,
            builder(irBlock.startOffset),
            externalPatch,
            statements.map { it.toAir(this@ArmBlock2Air) }
        )

        airClass.functions += builder

        return builder
    }

}