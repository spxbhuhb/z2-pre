package hu.simplexion.z2.kotlin.ir.rui.air2ir

import hu.simplexion.z2.kotlin.ir.rui.*
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilderBlock
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
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
        val symbolMap = RUI_FQN_BLOCK_CLASS.symbolMap

        val startScope = irFunction.valueParameters[RUI_BUILDER_ARGUMENT_INDEX_START_SCOPE]

        irFunction.body = DeclarationIrBuilder(irContext, irFunction.symbol).irBlockBody {

            val receiver = irFunction.dispatchReceiverParameter !!

            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    symbolMap.defaultType,
                    symbolMap.primaryConstructor.symbol,
                    0, 0,
                    RUI_BLOCK_ARGUMENT_COUNT // adapter, array of fragments
                ).also { constructorCall ->

                    constructorCall.putValueArgument(
                        RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER,
                        irGetValue(airClass.adapter, irGet(receiver))
                    )

                    constructorCall.putValueArgument(
                        RUI_BLOCK_ARGUMENT_INDEX_FRAGMENTS,
                        buildFragmentVarArg(startScope)
                    )

                }
            )
        }
    }

    fun AirBuilderBlock.buildFragmentVarArg(startScope: IrValueParameter): IrExpression {
        return IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(context.ruiFragmentType),
            context.ruiFragmentType,
        ).also { vararg ->
            subBuilders.forEach { sub ->

                val call = IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, // TODO use proper offsets from the original source code
                    context.ruiFragmentType,
                    sub.irFunction.symbol,
                    typeArgumentsCount = 0,
                    valueArgumentsCount = 1
                ).apply {
                    dispatchReceiver = irGet(irFunction.dispatchReceiverParameter !!)
                    putValueArgument(0, irGet(startScope))
                }

                vararg.addElement(call)
            }
        }
    }

}
