package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirInvokeBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSupportFunctionArgument
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors

class ArmSupportFunctionArgument2Air(
    parent: ClassBoundIrBuilder,
    val argument: ArmSupportFunctionArgument,
    val closure: ArmClosure,
    val fragment: IrValueParameter,
    val closureDirtyMask: IrVariable
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.invokeBranches += AirInvokeBranch(argument.argumentIndex, invokeBranch())
    }

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
                argument.dependencies.toDirtyMask()
            )
        )

    fun patchBody(): IrExpression =
        irSetDescendantStateVariable(
            argument.argumentIndex,
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                classBoundSupportFunctionType,
                pluginContext.adaptiveSupportFunctionClass.constructors.first(),
                1, 0,
                Indices.ADAPTIVE_SUPPORT_FUNCTION_ARGUMENT_COUNT,
            ).apply {
                putTypeArgument(0, classBoundBridgeType.defaultType)
                putValueArgument(Indices.ADAPTIVE_SUPPORT_FUNCTION_FRAGMENT, irGet(fragment))
                putValueArgument(Indices.ADAPTIVE_SUPPORT_FUNCTION_INDEX, irConst(argument.supportFunctionIndex))
            }
        )

    fun invokeBranch() =
        argument.irExpression.transformStateAccess(closure, external = false) { irGet(fragment) }

}