package hu.simplexion.z2.kotlin.ir.rui.air2ir

import hu.simplexion.z2.kotlin.ir.rui.*
import hu.simplexion.z2.kotlin.ir.rui.air.AirBuilderBlock
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
        val symbolMap = RUI_FQN_BLOCK_CLASS.symbolMap

        irFunction.body = DeclarationIrBuilder(irContext, irFunction.symbol).irBlockBody {

            val localScope = irFunction.dispatchReceiverParameter !!

            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    symbolMap.defaultType,
                    symbolMap.primaryConstructor.symbol,
                    0, 0,
                    RUI_BLOCK_ARGUMENT_COUNT // adapter, array of fragments
                ).also { constructorCall ->

                    constructorCall.putValueArgument(
                        Indices.RUI_FRAGMENT_ADAPTER,
                        irGetValue(airClass.adapter, irGet(localScope))
                    )

                    constructorCall.putValueArgument(
                        RUI_BLOCK_ARGUMENT_INDEX_FRAGMENTS,
                        buildFragmentVarArg()
                    )

                }
            )
        }
    }

    fun AirBuilderBlock.buildFragmentVarArg(): IrExpression {
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
                    valueArgumentsCount = 0
                ).apply {
                    dispatchReceiver = irGet(irFunction.dispatchReceiverParameter !!)
                }

                vararg.addElement(call)
            }
        }
    }

}
