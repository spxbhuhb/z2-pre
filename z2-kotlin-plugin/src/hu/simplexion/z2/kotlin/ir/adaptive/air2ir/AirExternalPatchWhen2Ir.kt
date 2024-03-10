package hu.simplexion.z2.kotlin.ir.adaptive.air2ir

import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirExternalPatchWhen
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody

/**
 * Generates external patch for branches.
 * */
class AirExternalPatchWhen2Ir(
    parent: ClassBoundIrBuilder,
    val externalPatch: AirExternalPatchWhen
) : ClassBoundIrBuilder(parent) {

    val rumWhen
        get() = externalPatch.rumElement

    val dispatchReceiver
        get() = externalPatch.irFunction.dispatchReceiverParameter!!

    val symbolMap = rumWhen.symbolMap(this)

    fun toIr() {
        val function = externalPatch.irFunction
        
        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            TODO()
        }
    }

}
