package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmCall
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmCallBuilder(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun : IrSimpleFunction) : IrExpression =
        irConstructorCallFromBuild(buildFun, armCall.target)

    override fun genPatchDescendantBranch(patchFun : IrSimpleFunction, closureMask: IrVariable) : IrBlock {
        val fragmentParameter = patchFun.valueParameters.first()

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->
                armCall.arguments.forEach {
                    block.statements += it.toPatchExpression(this, patchFun, armCall.closure, fragmentParameter, closureMask)
                }
            }
    }

    override fun genInvokeBranch(invokeFun: IrSimpleFunction, closure: IrVariable): IrExpression {
        return irNull()
    }
}
