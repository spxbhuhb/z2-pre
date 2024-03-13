package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderCall
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.StateAccessTransform.Companion.transformStateAccess
import org.jetbrains.kotlin.ir.builders.irReturn

class AirBuilderCall2Ir(
    parent: ClassBoundIrBuilder,
    val call: AirBuilderCall
) : AbstractAirBuilder(parent, call) {

    fun toIr() {
        val armCall = call.armElement

        buildBody {
            + irReturn(
                buildConstructorCall(
                    armCall.target.symbolMap,
                    armCall.valueArguments.size + Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT
                ) { constructorCall, declaringComponent ->

                    constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_EXTERNAL_PATCH, irExternalPatchReference(declaringComponent))

                    armCall.valueArguments.forEachIndexed { index, adaptiveExpression ->
                        constructorCall.putValueArgument(
                            index + Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT,
                            transformStateAccess(call.irFunction, adaptiveExpression, call.irFunction.dispatchReceiverParameter!!.symbol)
                        )
                    }
                }
            )
        }
    }

}
