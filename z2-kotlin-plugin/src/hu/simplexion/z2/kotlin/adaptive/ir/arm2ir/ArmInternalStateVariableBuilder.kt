package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmInternalStateVariable
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression

open class ArmInternalStateVariableBuilder(
    parent: ClassBoundIrBuilder,
    val stateVariable: ArmInternalStateVariable
) : ClassBoundIrBuilder(parent) {

    fun genInitializer(internalPatchFun: IrSimpleFunction): IrExpression {
        val producer = stateVariable.producer ?: return stateVariable.irVariable.initializer!!

        val call = stateVariable.irVariable.initializer as IrCall

        call.putValueArgument(
            producer.argumentIndex - 1,
            genStateValueBindingInstance(
                internalPatchFun,
                stateVariable.indexInState,
                stateVariable.indexInClosure,
                stateVariable.type,
                producer.supportFunctionIndex
            )
        )

        call.putValueArgument(producer.argumentIndex, irNull())

        return call
    }

}