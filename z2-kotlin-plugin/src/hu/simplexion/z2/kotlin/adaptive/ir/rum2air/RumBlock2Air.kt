package hu.simplexion.z2.kotlin.adaptive.ir.rum2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderBlock
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchBlock
import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumBlock

class RumBlock2Air(
    parent: ClassBoundIrBuilder,
    val rumBlock: RumBlock
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirBuilderBlock = with(rumBlock) {

        val externalPatch = AirExternalPatchBlock(
            rumBlock,
            externalPatch(irBlock.startOffset),
        )
        airClass.functions += externalPatch

        val builder = AirBuilderBlock(
            rumBlock,
            builder(irBlock.startOffset),
            externalPatch,
            statements.map { it.toAir(this@RumBlock2Air) }
        )

        airClass.functions += builder

        return builder
    }

}