package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSelect
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmSelect2Air(
    parent: ClassBoundIrBuilder,
    val armSelect: ArmSelect
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armSelect.index, irConstructorCallFromBuild(armSelect.target))
        airClass.patchBranches += AirPatchBranch(armSelect.index) { irPatchBranch() }
    }

    private fun irPatchBranch(): IrExpression =
        irSetDescendantStateVariable(
            Indices.ADAPTIVE_SELECT_BRANCH,
            irSelectWhen()
        )

    private fun irSelectWhen(): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            IrStatementOrigin.WHEN
        ).apply {

            armSelect.branches.forEachIndexed { branchIndex, branch ->
                branches += irConditionBranch(branchIndex, branch)
            }

            // add "else" if the last condition is not a constant true
            armSelect.branches.last().condition.irExpression.let {
                if (! (it is IrConst<*> && it.value is Boolean && it.value == true)) {
                    branches += irElseBranch()
                }
            }
        }

    private fun irConditionBranch(branchIndex: Int, branch: ArmBranch) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            branch.condition.irExpression.transformStateAccess(armSelect.closure) { irGet(airClass.patchDescendant.valueParameters.first()) },
            irConst(branch.index)
        )

    private fun irElseBranch() =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            irConst(- 1)
        )

}