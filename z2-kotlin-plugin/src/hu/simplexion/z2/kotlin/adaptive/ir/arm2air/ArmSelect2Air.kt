package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.*
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.StateAccessTransform.Companion.transformStateAccess
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSelect
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmSelect2Air(
    parent: ClassBoundIrBuilder,
    val armSelect: ArmSelect
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armSelect.index, irBuildExpression(armSelect.symbolMap(this)))
        airClass.patchBranches += AirPatchBranch(armSelect.index, irPatchExpression())
    }

    fun irPatchExpression(): IrBlock {

    }

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
            branches.last().condition.let {
                if (!(it is IrConst<*> && it.value is Boolean && it.value == true)) {
                    branches += irElseBranch()
                }
            }
        }

    private fun irConditionBranch(branchIndex: Int, branch: ArmBranch) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(irConst (branchIndex),
            transformStateAccess(airClass.patch, branch.condition),
        )

    fun irElseBranch() =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            irConst(- 1)
        )

}