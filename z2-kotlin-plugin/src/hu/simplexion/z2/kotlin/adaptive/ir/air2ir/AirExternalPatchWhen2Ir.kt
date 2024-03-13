package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchWhen
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.StateAccessTransform.Companion.transformStateAccess
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmBranch
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irAs
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

/**
 * Generates external patch for branches.
 * */
class AirExternalPatchWhen2Ir(
    parent: ClassBoundIrBuilder,
    val externalPatch: AirExternalPatchWhen
) : ClassBoundIrBuilder(parent) {

    val armWhen
        get() = externalPatch.armElement

    val dispatchReceiver
        get() = externalPatch.irFunction.dispatchReceiverParameter !!

    val symbolMap = armWhen.symbolMap(this)

    fun toIr() {
        val function = externalPatch.irFunction

        val externalPatchIt = function.valueParameters[Indices.ADAPTIVE_EXTERNAL_PATCH_FRAGMENT]

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            val adaptiveWhen = irTemporary(irAs(irGet(externalPatchIt), symbolMap.defaultType))

            + if (armWhen.irSubject == null) {
                irCall(
                    pluginContext.adaptiveWhenNewBranchSetter,
                    dispatchReceiver = irGet(adaptiveWhen),
                    args = arrayOf(irSelectWhen())
                )
            } else {
                TODO("when with subject")
            }
        }
    }

    private fun irSelectWhen(): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            IrStatementOrigin.WHEN
        ).apply {
            armWhen.branches.forEachIndexed { branchIndex, branch ->
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
            transformStateAccess(externalPatch.irFunction, branch.condition, dispatchReceiver.symbol),
            irConst(branchIndex)
        )

    fun irElseBranch() =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            irConst(- 1)
        )
}
