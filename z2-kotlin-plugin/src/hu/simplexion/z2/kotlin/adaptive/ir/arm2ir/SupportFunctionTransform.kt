/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSupportStateVariable
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class SupportFunctionTransform(
    private val irBuilder: ClassBoundIrBuilder,
    private val closure: ArmClosure,
    private val callingFragment: IrVariable,
    private val arguments: IrVariable
) : IrElementTransformerVoidWithContext() {

    val pluginContext = irBuilder.pluginContext

    val irContext = irBuilder.irContext

    val irBuiltIns = irContext.irBuiltIns

    override fun visitGetValue(expression: IrGetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val stateVariable = closure.firstOrNull { it.name == name } ?: return expression

        if (stateVariable is ArmSupportStateVariable) {
            return getInvokeArgument(stateVariable)
        } else {
            return getStateVariable(stateVariable)
        }
    }

    fun getInvokeArgument(stateVariable: ArmSupportStateVariable) : IrExpression {
        val type = irBuilder.stateVariableType(stateVariable)

        return irBuilder.irImplicitAs(
            type,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.anyNType,
                pluginContext.arrayGet,
                typeArgumentsCount = 0,
                valueArgumentsCount = 1
            ).also {

                it.dispatchReceiver = irBuilder.irGet(arguments)

                it.putValueArgument(
                    0,
                    irBuilder.irConst(stateVariable.indexInState)
                )
            }
        )
    }

    fun getStateVariable(stateVariable: ArmStateVariable) : IrExpression {
        val type = irBuilder.stateVariableType(stateVariable)

        return irBuilder.irImplicitAs(
            type,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.anyNType,
                pluginContext.getThisClosureVariable,
                0,
                Indices.GET_CLOSURE_VARIABLE_ARGUMENT_COUNT
            ).also {

                it.dispatchReceiver = irBuilder.irGet(callingFragment)

                it.putValueArgument(
                    Indices.GET_CLOSURE_VARIABLE_INDEX,
                    irBuilder.irConst(stateVariable.indexInClosure)
                )
            }
        )
    }

    override fun visitSetValue(expression: IrSetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val stateVariable = closure.firstOrNull { it.name == name } ?: return super.visitSetValue(expression)

        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            0,
            Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also {

            it.dispatchReceiver = irBuilder.irGet(callingFragment)

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

}
