package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrExpression

interface BranchBuilder {

    fun genBuildConstructorCall(buildFun : IrSimpleFunction) : IrExpression

    fun genPatchDescendantBranch(patchFun : IrSimpleFunction, closureMask: IrVariable): IrExpression

    fun genInvokeBranches(invokeFun: IrSimpleFunction, supportFunctionIndex: IrVariable, callingFragment: IrVariable, arguments: IrVariable) : List<IrBranch> {
        throw IllegalStateException("should not be called, this is a plugin error")
    }

}