package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmCall
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSupportFunctionArgument
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSupportStateVariable
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.statements

class ArmCallBuilder(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun: IrSimpleFunction): IrExpression =
        irConstructorCallFromBuild(buildFun, armCall.target)

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrBlock {
        val fragmentParameter = patchFun.valueParameters.first()

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->
                armCall.arguments.forEach {
                    block.statements += it.toPatchExpression(this, patchFun, armCall.closure, fragmentParameter, closureMask)
                }
            }
    }

    override fun genInvokeBranches(
        invokeFun: IrSimpleFunction,
        supportFunctionIndex: IrVariable,
        callingFragment: IrVariable,
        arguments: IrVariable
    ): List<IrBranch> =
        armCall.arguments
            .filterIsInstance<ArmSupportFunctionArgument>()
            .map { genInvokeBranch(invokeFun, supportFunctionIndex, callingFragment, arguments, it) }

    private fun genInvokeBranch(
        invokeFun: IrSimpleFunction,
        supportFunctionIndex: IrVariable,
        callingFragment: IrVariable,
        arguments: IrVariable,
        armSupportFunctionArgument: ArmSupportFunctionArgument
    ): IrBranch =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(supportFunctionIndex),
                irConst(armSupportFunctionArgument.supportFunctionIndex)
            ),
            genInvokeBranchBody(invokeFun, callingFragment, arguments, armSupportFunctionArgument)
        )

    private fun genInvokeBranchBody(
        invokeFun: IrSimpleFunction,
        callingFragment: IrVariable,
        arguments: IrVariable,
        armSupportFunctionArgument: ArmSupportFunctionArgument
    ): IrExpression {
        val functionToTransform = (armSupportFunctionArgument.irExpression as IrFunctionExpression).function
        val originalClosure = armSupportFunctionArgument.supportFunctionClosure

        val transformClosure = originalClosure + functionToTransform.valueParameters.mapIndexed { indexInState, parameter ->
            ArmSupportStateVariable(
                armCall.armClass,
                indexInState,
                originalClosure.size + indexInState,
                parameter
            )
        }

        return IrBlockImpl(
            functionToTransform.startOffset, functionToTransform.endOffset,
            functionToTransform.returnType
        ).apply {
            val transform = SupportFunctionTransform(this@ArmCallBuilder, transformClosure, callingFragment, arguments)

            functionToTransform.body!!.statements.forEach {
                statements += it.transformStatement(transform)
            }
        }
    }
}