package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchSequence
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody

/**
 * External patch generation for sequences. This is just an empty function as sequences do not have
 * state, therefore there is nothing to patch. Sequences call the external patch of the fragments
 * they contain from `patch`.
 */
class AirExternalPatchSequence2Ir(
    parent: ClassBoundIrBuilder,
    val externalPatch: AirExternalPatchSequence
) : ClassBoundIrBuilder(parent) {

    fun toIr() {
        externalPatch.irFunction.body = DeclarationIrBuilder(irContext, externalPatch.irFunction.symbol).irBlockBody { }
    }

}
