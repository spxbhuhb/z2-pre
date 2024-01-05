package hu.simplexion.z2.kotlin.ir.rui.air2ir

import hu.simplexion.z2.kotlin.ir.rui.*
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilderCall
import hu.simplexion.z2.kotlin.ir.rui.air2ir.StateAccessTransform.Companion.transformStateAccess
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType

class AirBuilderCall2Ir(
    val parent: ClassBoundIrBuilder,
    val call: AirBuilderCall
) : ClassBoundIrBuilder(parent) {

    fun toIr() {
        val rumCall = call.rumElement
        val symbolMap = rumCall.target.symbolMap
        val irFunction = call.irFunction

        irFunction.body = DeclarationIrBuilder(irContext, irFunction.symbol).irBlockBody {

            val localScope = irFunction.dispatchReceiverParameter !! // the class this builder is member of

            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    symbolMap.defaultType,
                    symbolMap.primaryConstructor.symbol,
                    typeArgumentsCount = 1, // bridge type
                    constructorTypeArgumentsCount = 0,
                    rumCall.valueArguments.size + RUI_FRAGMENT_ARGUMENT_COUNT // +3 = adapter + scope + external patch
                ).also { constructorCall ->

                    constructorCall.putTypeArgument(RUI_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)

                    constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER, irGetValue(airClass.adapter, irGet(localScope)))
                    constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_SCOPE, irGetValue(airClass.scope, irGet(localScope)))
                    constructorCall.putValueArgument(RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH, irExternalPatchReference(localScope))

                    rumCall.valueArguments.forEachIndexed { index, ruiExpression ->
                        constructorCall.putValueArgument(
                            index + RUI_FRAGMENT_ARGUMENT_COUNT,
                            transformStateAccess(ruiExpression, localScope.symbol)
                        )
                    }
                }
            )
        }
    }

    fun irExternalPatchReference(localScope: IrValueParameter): IrExpression =
        IrFunctionReferenceImpl.fromSymbolOwner(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundExternalPatchType,
            call.externalPatch.irFunction.symbol,
            typeArgumentsCount = 0,
            reflectionTarget = call.externalPatch.irFunction.symbol
        ).also {
            it.dispatchReceiver = irImplicitAs(parent.irClass.defaultType,irGet(localScope))
        }
}
