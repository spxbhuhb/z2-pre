package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmStateVariableBindingArgument
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

class ArmStateVariableBindingArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmStateVariableBindingArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    override fun patchBody(patchFun: IrSimpleFunction): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            argument.argumentIndex,
            genStateVariableBindingInstance(
                patchFun,
                argument.indexInState,
                argument.indexInClosure,
                argument.boundType,
                argument.supportFunctionIndex
            )
        )

}