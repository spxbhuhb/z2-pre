package hu.simplexion.z2.kotlin.ir.adaptive.rum2air

import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirBuilderBlock
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirExternalPatchBlock
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumBlock

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