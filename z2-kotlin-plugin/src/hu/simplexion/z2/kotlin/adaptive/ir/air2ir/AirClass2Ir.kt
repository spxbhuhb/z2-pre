package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirInvokeBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchDescendantBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmInternalStateVariable
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
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

        // adds build, patch and invoke branches
        // this has to be done after all classes has their IrClass or build won't be able to find them
        airClass.armClass.rendering.forEach { it.toAir(this) }

        build()
        patchDescendant()
        invoke()
        patchInternal()

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
            classBoundFragmentType,
            IrStatementOrigin.WHEN
        ).apply {

            airClass.buildBranches.forEach { branch ->
                branches += irBuildConditionBranch(branch)
            }

            branches += irInvalidIndexBranch(airClass.build, irGet(airClass.build.valueParameters[Indices.BUILD_DECLARATION_INDEX]))
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
    // Patch Descendants
    // ---------------------------------------------------------------------------

    fun patchDescendant() {
        val patchFun = airClass.patchDescendant

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            val closureMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.getCreateClosureDirtyMask,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.valueParameters[Indices.PATCH_EXTERNAL_FRAGMENT])
                }
            )

            val fragmentIndex = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.index.single().owner.getter !!.symbol,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.valueParameters[Indices.PATCH_EXTERNAL_FRAGMENT])
                }
            )

            + patchDescendantWhen(fragmentIndex, closureMask)
        }
    }

    private fun patchDescendantWhen(fragmentIndex: IrVariable, closureMask: IrVariable): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            IrStatementOrigin.WHEN
        ).apply {

            airClass.patchDescendantBranches.forEach { branch ->
                branches += irPatchDescendantBranch(branch, fragmentIndex, closureMask)
            }

            branches += irInvalidIndexBranch(airClass.patchDescendant, irGet(fragmentIndex))
        }

    private fun irPatchDescendantBranch(branch: AirPatchDescendantBranch, fragmentIndex: IrVariable, closureMask: IrVariable) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(fragmentIndex),
                irConst(branch.index)
            ),
            branch.branchBuilder(closureMask)
        )

    // ---------------------------------------------------------------------------
    // Invoke
    // ---------------------------------------------------------------------------

    fun invoke() {
        val invokeFun = airClass.generatedInvoke

        invokeFun.body = DeclarationIrBuilder(irContext, invokeFun.symbol).irBlockBody {

            if (airClass.invokeBranches.isEmpty()) {
                + irShouldNotRun(invokeFun)
                return@irBlockBody
            }


            val supportFunctionIndex = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.adaptiveSupportFunctionIndex,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(invokeFun.valueParameters.first())
                }
            )

            val fragment = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    classBoundFragmentType,
                    pluginContext.adaptiveSupportFunctionFragment,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(invokeFun.valueParameters.first())
                }
            )

            + invokeWhen(invokeFun, supportFunctionIndex, fragment)
        }
    }

    private fun invokeWhen(invokeFun: IrSimpleFunction, supportFunctionIndex: IrVariable, closure: IrVariable): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            IrStatementOrigin.WHEN
        ).apply {

            airClass.invokeBranches.forEach { branch ->
                branches += irInvokeBranch(branch, supportFunctionIndex, closure)
            }

            branches += irInvalidIndexBranch(airClass.patchDescendant, irGet(supportFunctionIndex))
        }

    private fun irInvokeBranch(branch: AirInvokeBranch, supportFunctionIndex: IrVariable, closure: IrVariable) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(supportFunctionIndex),
                irConst(branch.index)
            ),
            branch.invokeBuilder(closure)
        )

    // ---------------------------------------------------------------------------
    // Patch Internal
    // ---------------------------------------------------------------------------

    fun patchInternal() {
        val patchFun = airClass.generatedPatchInternal

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            airClass.armClass.stateDefinitionStatements.forEach {
                val originalExpression = if (it is ArmInternalStateVariable) it.irVariable.initializer !! else it.irStatement
                val transformedExpression = originalExpression.transformStateAccess(airClass.armClass.stateVariables, external = false) { irGet(patchFun.dispatchReceiverParameter !!) }

                if (it is ArmInternalStateVariable) {
                    + irSetInternalStateVariable(it.indexInState, transformedExpression as IrExpression)
                } else {
                    + transformedExpression
                }
            }

            + irReturn(irUnit())
        }
    }

    // ---------------------------------------------------------------------------
    // Common
    // ---------------------------------------------------------------------------


    private fun irShouldNotRun(fromFun: IrSimpleFunction) =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.nothingType,
            airClass.irClass.getSimpleFunction(Strings.SHOULD_NOT_RUN) !!,
            0, 0
        ).also {
            it.dispatchReceiver = irGet(fromFun.dispatchReceiverParameter !!)
        }

    private fun irInvalidIndexBranch(fromFun: IrSimpleFunction, getIndex: IrExpression) =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.nothingType,
                airClass.irClass.getSimpleFunction(Strings.INVALID_INDEX) !!,
                0, 1
            ).also {
                it.dispatchReceiver = irGet(fromFun.dispatchReceiverParameter !!)
                it.putValueArgument(
                    Indices.INVALID_INDEX_INDEX,
                    getIndex
                )
            }
        )

}

