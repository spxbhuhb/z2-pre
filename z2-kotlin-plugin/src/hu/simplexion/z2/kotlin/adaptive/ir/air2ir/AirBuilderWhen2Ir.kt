package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderWhen
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType

class AirBuilderWhen2Ir(
    parent: ClassBoundIrBuilder,
    val builder: AirBuilderWhen
) : AbstractAirBuilder(parent, builder) {

    fun toIr() {
        buildBody {
            + irReturn(
                buildConstructorCall(
                    FqNames.ADAPTIVE_WHEN_CLASS.symbolMap,
                    Indices.ADAPTIVE_WHEN_ARGUMENT_COUNT
                ) { constructorCall, declaringComponent ->
                    constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_EXTERNAL_PATCH, irExternalPatchReference(declaringComponent))
                    constructorCall.putValueArgument(Indices.ADAPTIVE_WHEN_FACTORY_ARG, irFactoryReference(declaringComponent))
                }
            )
        }
    }

    fun irFactoryReference(scope : IrValueParameter) =
        IrFunctionReferenceImpl.fromSymbolOwner(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundFragmentFactoryType,
            builder.fragmentFactory.irFunction.symbol,
            typeArgumentsCount = 0,
            reflectionTarget = builder.fragmentFactory.irFunction.symbol
        ).also {
            it.dispatchReceiver = irImplicitAs(irClass.defaultType, irGet(scope))
        }

}
