package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSelect
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmSelectBuilder(
    parent: ClassBoundIrBuilder,
    val armSelect: ArmSelect
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun : IrSimpleFunction) : IrExpression =
        irConstructorCallFromBuild(buildFun, armSelect.target)

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            Indices.ADAPTIVE_SELECT_BRANCH,
            irSelectWhen(patchFun)
        )

    private fun irSelectWhen(patchFun: IrSimpleFunction): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            IrStatementOrigin.WHEN
        ).apply {

            armSelect.branches.forEach { branch ->
                branches += irConditionBranch(patchFun, branch)
            }

            // add "else" if the last condition is not a constant true
            armSelect.branches.last().condition.irExpression.let {
                if (! (it is IrConst<*> && it.value is Boolean && it.value == true)) {
                    branches += irElseBranch()
                }
            }
        }

    private fun irConditionBranch(patchFun: IrSimpleFunction, branch: ArmBranch) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            branch.condition.irExpression.transformStateAccess(armSelect.closure, external = true) { irGet(patchFun.valueParameters.first()) },
            irConst(branch.index)
        )

    private fun irElseBranch() =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            irConst(- 1)
        )

}