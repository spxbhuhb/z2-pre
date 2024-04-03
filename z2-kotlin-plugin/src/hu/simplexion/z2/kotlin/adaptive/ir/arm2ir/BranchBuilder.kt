package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

interface BranchBuilder {

    fun genBuildConstructorCall(buildFun : IrSimpleFunction) : IrExpression

    fun genPatchDescendantBranch(patchFun : IrSimpleFunction, closureMask: IrVariable): IrExpression

    fun genInvokeBranch(invokeFun : IrSimpleFunction, closure : IrVariable) : IrExpression {
        throw IllegalStateException("should not be called, this is a plugin error")
    }

}