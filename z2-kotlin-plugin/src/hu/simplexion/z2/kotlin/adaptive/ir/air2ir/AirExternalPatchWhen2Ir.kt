package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchWhen
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irAs
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irTemporary

/**
 * Generates external patch for branches.
 * */
class AirExternalPatchWhen2Ir(
    parent: ClassBoundIrBuilder,
    val externalPatch: AirExternalPatchWhen
) : ClassBoundIrBuilder(parent) {

    val armWhen
        get() = externalPatch.armElement

    val dispatchReceiver
        get() = externalPatch.irFunction.dispatchReceiverParameter !!

    val symbolMap = armWhen.symbolMap(this)

    fun toIr() {
        val function = externalPatch.irFunction

        val externalPatchIt = function.valueParameters[Indices.ADAPTIVE_EXTERNAL_PATCH_FRAGMENT]

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            val adaptiveWhen = irTemporary(irAs(irGet(externalPatchIt), symbolMap.defaultType))

            + if (armWhen.irSubject == null) {
                irCall(
                    pluginContext.adaptiveWhenNewBranchSetter,
                    dispatchReceiver = irGet(adaptiveWhen),
                    args = arrayOf(irSelectWhen())
                )
            } else {
                TODO("when with subject")
            }
        }
    }


}
