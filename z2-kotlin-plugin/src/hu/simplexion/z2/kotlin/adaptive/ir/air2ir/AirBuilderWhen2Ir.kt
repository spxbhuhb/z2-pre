package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilderSequence
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable

class AirBuilderWhen2Ir(
    parent: ClassBoundIrBuilder,
    val builder: AirBuilderSequence
) : AbstractAirBuilder(parent, builder) {

    fun toIr() {
        buildBody {

            val sequence = irTemporary(
                buildConstructorCall(
                    FqNames.ADAPTIVE_SEQUENCE_CLASS.symbolMap,
                    Indices.ADAPTIVE_SEQUENCE_ARGUMENT_COUNT
                )
            )

            buildFragments(airBuilder.irFunction.dispatchReceiverParameter !!, sequence)

            + irReturn(irGet(sequence))
        }
    }

    fun IrBlockBodyBuilder.buildFragments(declaringComponent: IrValueParameter, sequence: IrVariable) {
        airBuilder.subBuilders.forEach { sub ->
            + irCall(
                pluginContext.adaptiveSequenceAddFun,
                dispatchReceiver = irGet(sequence),
                args = arrayOf(
                    irCall(
                        sub.irFunction.symbol,
                        dispatchReceiver = irGet(declaringComponent),
                        args = arrayOf(irGet(sequence))
                    )
                )
            )
        }
    }

}
