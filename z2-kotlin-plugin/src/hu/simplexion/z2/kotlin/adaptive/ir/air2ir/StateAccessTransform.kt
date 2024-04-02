/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
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

        val name = expression.symbol.owner.name.identifier

        val stateVariable = closure.firstOrNull { it.name == name } ?: return expression

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
     * Transform calls in `patchInternal`:
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
