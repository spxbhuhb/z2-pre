/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmStateVariable
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class StateAccessTransform(
    private val irBuilder: ClassBoundIrBuilder,
    private val closure: ArmClosure,
    private val external: Boolean,
    private val irGetFragment: () -> IrExpression
) : IrElementTransformerVoidWithContext() {

    val pluginContext = irBuilder.pluginContext

    val irContext = irBuilder.irContext

    val irBuiltIns = irContext.irBuiltIns

    override fun visitGetValue(expression: IrGetValue): IrExpression {

        //  FUN name:higherFun visibility:public modality:FINAL <> ($receiver:hu.simplexion.z2.adaptive.Adaptive, higherI:kotlin.Int, lowerFun:@[ExtensionFunctionType] kotlin.Function2<hu.simplexion.z2.adaptive.Adaptive, @[ParameterName(name = 'lowerFunI')] kotlin.Int, kotlin.Unit>) returnType:kotlin.Unit
        //      $receiver: VALUE_PARAMETER name:<this> type:hu.simplexion.z2.adaptive.Adaptive
        //      VALUE_PARAMETER name:higherI index:0 type:kotlin.Int
        //      VALUE_PARAMETER name:lowerFun index:1 type:@[ExtensionFunctionType] kotlin.Function2<hu.simplexion.z2.adaptive.Adaptive, @[ParameterName(name = 'lowerFunI')] kotlin.Int, kotlin.Unit>
        //      BLOCK_BODY
        //        CALL 'public final fun higherFunInner (innerI: kotlin.Int, lowerFunInner: @[ExtensionFunctionType] kotlin.Function2<hu.simplexion.z2.adaptive.Adaptive, @[ParameterName(name = 'lowerFunInnerI')] kotlin.Int, kotlin.Unit>): kotlin.Unit declared in hu.simplexion.z2.kotlin.adaptive.success' type=kotlin.Unit origin=null
        //          $receiver: GET_VAR '<this>: hu.simplexion.z2.adaptive.Adaptive declared in hu.simplexion.z2.kotlin.adaptive.success.higherFun' type=hu.simplexion.z2.adaptive.Adaptive origin=null
        //          innerI: CALL 'public final fun times (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=MUL
        //            $this: GET_VAR 'higherI: kotlin.Int declared in hu.simplexion.z2.kotlin.adaptive.success.higherFun' type=kotlin.Int origin=null
        //            other: CONST Int type=kotlin.Int value=2
        //          lowerFunInner: FUN_EXPR type=@[ExtensionFunctionType] kotlin.Function2<hu.simplexion.z2.adaptive.Adaptive, @[ParameterName(name = 'lowerFunInnerI')] kotlin.Int, kotlin.Unit> origin=LAMBDA
        //            FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> ($receiver:hu.simplexion.z2.adaptive.Adaptive, lowerFunInnerI:@[ParameterName(name = 'lowerFunInnerI')] kotlin.Int) returnType:kotlin.Unit
        //              $receiver: VALUE_PARAMETER name:$this$higherFunInner type:hu.simplexion.z2.adaptive.Adaptive
        //              VALUE_PARAMETER name:lowerFunInnerI index:0 type:@[ParameterName(name = 'lowerFunInnerI')] kotlin.Int
        //              BLOCK_BODY
        //                CALL 'public abstract fun invoke (p1: P1 of kotlin.Function2, p2: P2 of kotlin.Function2): R of kotlin.Function2 [operator] declared in kotlin.Function2' type=kotlin.Unit origin=INVOKE
        //                  $this: GET_VAR 'lowerFun: @[ExtensionFunctionType] kotlin.Function2<hu.simplexion.z2.adaptive.Adaptive, @[ParameterName(name = 'lowerFunI')] kotlin.Int, kotlin.Unit> declared in hu.simplexion.z2.kotlin.adaptive.success.higherFun' type=@[ExtensionFunctionType] kotlin.Function2<hu.simplexion.z2.adaptive.Adaptive, @[ParameterName(name = 'lowerFunI')] kotlin.Int, kotlin.Unit> origin=VARIABLE_AS_FUNCTION
        //                  p1: GET_VAR '$this$higherFunInner: hu.simplexion.z2.adaptive.Adaptive declared in hu.simplexion.z2.kotlin.adaptive.success.higherFun.<anonymous>' type=hu.simplexion.z2.adaptive.Adaptive origin=null
        //                  p2: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
        //                    $this: GET_VAR 'higherI: kotlin.Int declared in hu.simplexion.z2.kotlin.adaptive.success.higherFun' type=kotlin.Int origin=null
        //                    other: GET_VAR 'lowerFunInnerI: @[ParameterName(name = 'lowerFunInnerI')] kotlin.Int declared in hu.simplexion.z2.kotlin.adaptive.success.higherFun.<anonymous>' type=@[ParameterName(name = 'lowerFunInnerI')] kotlin.Int origin=null

        val name = expression.symbol.owner.name
        if (name.isSpecial) {
            return expression
        }

        val id = name.identifier

        val stateVariable = closure.firstOrNull { it.name == id } ?: return expression

        return getStateVariable(stateVariable)
    }

    fun getStateVariable(stateVariable: ArmStateVariable) : IrExpression {
        val type = irBuilder.stateVariableType(stateVariable)

        return irBuilder.irImplicitAs(
            type,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.anyNType,
                if (external) pluginContext.getCreateClosureVariable else pluginContext.getThisClosureVariable,
                0,
                Indices.GET_CLOSURE_VARIABLE_ARGUMENT_COUNT
            ).also {

                it.dispatchReceiver = irGetFragment()

                it.putValueArgument(
                    Indices.GET_CLOSURE_VARIABLE_INDEX,
                    irBuilder.irConst(stateVariable.indexInClosure)
                )
            }
        )
    }

    override fun visitSetValue(expression: IrSetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val stateVariable = closure.firstOrNull { it.name == name } ?: return expression

        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            0,
            Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also {

            it.dispatchReceiver = irGetFragment()

            it.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irBuilder.irConst(stateVariable.indexInState)
            )

            it.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                expression
            )
        }
    }

    /**
     * Transform calls in `genPatchInternal`:
     *
     * ```kotlin
     * fun Adaptive.Basic(i : Int, supportFun : (i : Int) -> Unit) {
     *     supportFun(i)
     * }
     * ```
     *
     * ```kotlin
     * fun patchInternal() {
     *     getThisStateVariable(1).invoke(getThisStateVariable(0))
     * }
     * ```
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (external) return super.visitCall(expression)

        val getValue = expression.dispatchReceiver as? IrGetValue ?: return expression
        val valueParameterSymbol = getValue.symbol as? IrValueParameterSymbol ?: return expression

        val stateVariable = closure.first { it.name == valueParameterSymbol.owner.name.identifier }

        return IrCallImpl(
            expression.startOffset, expression.endOffset,
            expression.type,
            pluginContext.adaptiveSupportFunctionInvoke,
            0, 1
        ).apply {
            dispatchReceiver = getStateVariable(stateVariable)

            putValueArgument(0,
                IrVarargImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.arrayClass.typeWith(irBuiltIns.anyNType),
                    irBuiltIns.anyNType
                ).apply {
                    for (argument in expression.valueArguments) {
                        // FIXME null handling in StateAccessTransform
                        elements += (argument ?: irBuilder.irNull()).accept(this@StateAccessTransform, null) as IrVarargElement
                    }
                }
            )
        }

    }

}
