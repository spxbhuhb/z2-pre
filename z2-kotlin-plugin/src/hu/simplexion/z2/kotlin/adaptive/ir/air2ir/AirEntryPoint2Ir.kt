package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirEntryPoint
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.getPropertyGetter

class AirEntryPoint2Ir(
    pluginContext: AdaptivePluginContext,
    entryPoint: AirEntryPoint
) : ClassBoundIrBuilder(pluginContext, entryPoint.airClass) {

    val function = entryPoint.armEntryPoint.irFunction
    val armClass = airClass.armClass

    fun toIr() {

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {

            val instance = IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                airClass.irClass.defaultType,
                airClass.constructor.symbol,
                0, 0,
                Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT
            ).also { call ->
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGetAdapter(function))
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irNull())
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irConst(0))
            }

            val root = irTemporary(instance, "root").also { it.parent = function }

            +irCall(
                pluginContext.create,
                dispatchReceiver = irGet(root)
            )

            +irCall(
                pluginContext.mount,
                dispatchReceiver = irGet(root),
                args = arrayOf(
                    irCall(
                        this@AirEntryPoint2Ir.pluginContext.adaptiveAdapterClass.getPropertyGetter(Strings.ROOT_BRIDGE)!!.owner.symbol,
                        dispatchReceiver = irGetAdapter(function)
                    )
                )
            )
        }

    }

    private fun irGetAdapter(function: IrSimpleFunction): IrExpression =
        IrGetValueImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            function.valueParameters.first().symbol
        )

}