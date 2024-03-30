package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchBranch
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.getSimpleFunction

class AirClass2Ir(
    context: AdaptivePluginContext,
    airClass: AirClass
) : ClassBoundIrBuilder(context, airClass) {

    fun toIr(): IrClass {

        build()
        patchExternal()
        invoke()

        return irClass
    }

    // ---------------------------------------------------------------------------
    // Build
    // ---------------------------------------------------------------------------

    fun build() {
        val buildFun = airClass.build

        buildFun.body = DeclarationIrBuilder(irContext, buildFun.symbol).irBlockBody {
            val fragment = irTemporary(irBuildWhen())

            + IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.unitType,
                pluginContext.create,
                0, 0
            ).also {
                it.dispatchReceiver = irGet(fragment)
            }

            + irReturn(irGet(fragment))
        }
    }

    private fun irBuildWhen(): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            IrStatementOrigin.WHEN
        ).apply {

            airClass.buildBranches.forEach{ branch ->
                branches += irBuildConditionBranch(branch)
            }

            branches += irInvalidIndexBranch(irGet(airClass.build.valueParameters[Indices.BUILD_DECLARATION_INDEX]))
        }

    private fun irBuildConditionBranch(branch: AirBuildBranch) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(airClass.build.valueParameters[Indices.BUILD_DECLARATION_INDEX]),
                irConst(branch.index)
            ),
            branch.expression
        )

    // ---------------------------------------------------------------------------
    // Patch
    // ---------------------------------------------------------------------------

    fun patchExternal() {
        val patchFun = airClass.patchExternal

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {
            val closureMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.getClosureMask,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.valueParameters[Indices.PATCH_EXTERNAL_FRAGMENT])
                }
            )

            val fragmentIndex = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.index.single().owner.getter!!.symbol,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.valueParameters[Indices.PATCH_EXTERNAL_FRAGMENT])
                }
            )

            patchExternalWhen(fragmentIndex, closureMask)
        }
    }

    private fun patchExternalWhen(fragmentIndex : IrVariable, closureMask: IrVariable): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            IrStatementOrigin.WHEN
        ).apply {

            airClass.patchBranches.forEach { branch ->
                branches += irPatchExternalBranch(branch, fragmentIndex, closureMask)
            }

            branches += irInvalidIndexBranch(irGet(fragmentIndex))
        }

    private fun irPatchExternalBranch(branch: AirPatchBranch, fragmentIndex: IrVariable, closureMask: IrVariable) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(fragmentIndex),
                irConst(branch.index)
            ),
            branch.irExpression
        )

    // ---------------------------------------------------------------------------
    // Invoke
    // ---------------------------------------------------------------------------

    fun invoke() {
        val invokeFun = airClass.invoke

        invokeFun.body = DeclarationIrBuilder(irContext, invokeFun.symbol).irBlockBody {
            + irReturn(irNull())
        }
    }

    // ---------------------------------------------------------------------------
    // Common
    // ---------------------------------------------------------------------------

    private fun irInvalidIndexBranch(getIndex : IrExpression) =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.nothingType,
                airClass.irClass.getSimpleFunction(Strings.INVALID_INDEX)!!,
                0, 1
            ).also {
                it.dispatchReceiver = irGet(airClass.build.dispatchReceiverParameter!!)
                it.putValueArgument(
                    Indices.INVALID_INDEX_INDEX,
                    getIndex
                )
            }
        )

}

