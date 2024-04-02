package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmValueArgument
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

class ArmValueArgument2Air(
    parent: ClassBoundIrBuilder,
    val valueArgument: ArmValueArgument,
    val closure: ArmClosure,
    val fragment: IrValueParameter,
    val closureDirtyMask: IrVariable
) : ClassBoundIrBuilder(parent) {

    fun toPatchDescendantExpression(): IrExpression =
        irIf(
            patchCondition(),
            patchBody()
        )

    fun patchCondition(): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(fragment),
            args = arrayOf(
                irGet(closureDirtyMask),
                valueArgument.dependencies.toDirtyMask()
            )
        )

    fun patchBody(): IrExpression =
        irSetDescendantStateVariable(
            valueArgument.argumentIndex,
            valueArgument.irExpression.transformStateAccess(closure, external = true) { irGet(fragment) }
        )
}