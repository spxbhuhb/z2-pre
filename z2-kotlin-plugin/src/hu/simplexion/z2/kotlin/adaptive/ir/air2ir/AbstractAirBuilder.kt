package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptiveClassSymbols
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType

open class AbstractAirBuilder(
    val parent: ClassBoundIrBuilder,
    val airBuilder: AirBuilder
) : ClassBoundIrBuilder(parent) {

    fun buildBody(
        builderFun: IrBlockBodyBuilder.() -> Unit
    ) {
        val irFunction = airBuilder.irFunction

        irFunction.body = DeclarationIrBuilder(irContext, irFunction.symbol).irBlockBody {
            builderFun()
        }
    }

    fun IrBlockBodyBuilder.buildConstructorCall(
        symbolMap: AdaptiveClassSymbols,
        valueArgumentsCount: Int,
        builderFun: IrBlockBodyBuilder.(constructorCall: IrConstructorCallImpl, declaringComponent: IrValueParameter) -> Unit = { _, _ -> }
    ): IrConstructorCallImpl {
        val irFunction = airBuilder.irFunction
        val declaringComponent = irFunction.dispatchReceiverParameter !! // the class this builder is member of

        return IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            symbolMap.defaultType,
            symbolMap.primaryConstructor.symbol,
            typeArgumentsCount = 1, // bridge type
            constructorTypeArgumentsCount = 0,
            valueArgumentsCount
        ).also { constructorCall ->

            constructorCall.putTypeArgument(Indices.ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)

            constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGetValue(airClass.adapter, irGet(declaringComponent)))
            constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_CLOSURE, irGetValue(airClass.closure, irGet(declaringComponent)))
            constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(irFunction.valueParameters[Indices.ADAPTIVE_BUILDER_PARENT]))

            builderFun(constructorCall, declaringComponent)
        }
    }

    fun irExternalPatchReference(scope: IrValueParameter): IrExpression =
        IrFunctionReferenceImpl.fromSymbolOwner(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundExternalPatchType,
            airBuilder.externalPatch.irFunction.symbol,
            typeArgumentsCount = 0,
            reflectionTarget = airBuilder.externalPatch.irFunction.symbol
        ).also {
            it.dispatchReceiver = irImplicitAs(irClass.defaultType, irGet(scope))
        }

}