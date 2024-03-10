package hu.simplexion.z2.kotlin.ir.adaptive.air2ir

import hu.simplexion.z2.kotlin.ir.adaptive.*
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirBuilderBlock
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.addElement
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class AirBuilderBlock2Ir(
    val parent: ClassBoundIrBuilder,
    val builder: AirBuilderBlock
) : ClassBoundIrBuilder(parent) {

    fun toIr() = with(builder) {
        val symbolMap = ADAPTIVE_FQN_BLOCK_CLASS.symbolMap

        irFunction.body = DeclarationIrBuilder(irContext, irFunction.symbol).irBlockBody {

            val localScope = irFunction.dispatchReceiverParameter !!

            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    symbolMap.defaultType,
                    symbolMap.primaryConstructor.symbol,
                    0, 0,
                    ADAPTIVE_BLOCK_ARGUMENT_COUNT // adapter, array of fragments
                ).also { constructorCall ->

                    constructorCall.putValueArgument(
                        Indices.ADAPTIVE_FRAGMENT_ADAPTER,
                        irGetValue(airClass.adapter, irGet(localScope))
                    )

                    constructorCall.putValueArgument(
                        ADAPTIVE_BLOCK_ARGUMENT_INDEX_FRAGMENTS,
                        buildFragmentVarArg()
                    )

                }
            )
        }
    }

    fun AirBuilderBlock.buildFragmentVarArg(): IrExpression {
        return IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(context.adaptiveFragmentType),
            context.adaptiveFragmentType,
        ).also { vararg ->
            subBuilders.forEach { sub ->

                val call = IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, // TODO use proper offsets from the original source code
                    context.adaptiveFragmentType,
                    sub.irFunction.symbol,
                    typeArgumentsCount = 0,
                    valueArgumentsCount = 0
                ).apply {
                    dispatchReceiver = irGet(irFunction.dispatchReceiverParameter !!)
                }

                vararg.addElement(call)
            }
        }
    }

}
