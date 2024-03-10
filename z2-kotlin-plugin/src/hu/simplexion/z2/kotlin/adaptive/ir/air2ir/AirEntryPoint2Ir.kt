package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirEntryPoint
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.name.Name

class AirEntryPoint2Ir(
    context: AdaptivePluginContext,
    entryPoint: AirEntryPoint
) : ClassBoundIrBuilder(context, entryPoint.airClass) {

    val function = entryPoint.rumEntryPoint.irFunction
    val rumClass = airClass.rumClass

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
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_CLOSURE, irNull())
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irNull())
                call.putValueArgument(Indices.ADAPTIVE_FRAGMENT_EXTERNAL_PATCH, irExternalPatch(function.symbol))
            }

            val root = irTemporary(instance, "root").also { it.parent = function }

            +irCall(
                Strings.ADAPTIVE_CREATE_FUN.function.symbol,
                dispatchReceiver = irGet(root)
            )

            +irCall(
                Strings.ADAPTIVE_MOUNT_FUN.function.symbol,
                dispatchReceiver = irGet(root),
                args = arrayOf(
                    irCall(
                        this@AirEntryPoint2Ir.context.adaptiveAdapterClass.getPropertyGetter(Strings.ADAPTIVE_ROOT_BRIDGE)!!.owner.symbol,
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

    fun irExternalPatch(parent: IrSimpleFunctionSymbol): IrExpression {
        val function = irFactory.buildFun {
            name = Name.special("<anonymous>")
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            returnType = irBuiltIns.longType
        }.also { function ->

            function.parent = parent.owner
            function.visibility = DescriptorVisibilities.LOCAL

            function.addValueParameter {
                name = Name.identifier("it")
                type = context.adaptiveFragmentType
            }

            function.body = DeclarationIrBuilder(context.irContext, function.symbol).irBlockBody {
                // +irReturn(irConst(0L))
            }
        }

        return IrFunctionExpressionImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundExternalPatchType,
            function,
            IrStatementOrigin.LAMBDA
        )
    }

}