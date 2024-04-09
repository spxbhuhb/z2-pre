package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmCall
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSupportFunctionArgument
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSupportStateVariable
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.ir.util.statements

class ArmCallBuilder(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun: IrSimpleFunction): IrExpression =
        if (armCall.isDirect) {
            irConstructorCallFromBuild(buildFun, armCall.target)
        } else {
            irConstructorCallFromBuild(
                buildFun,
                FqNames.ADAPTIVE_ANONYMOUS,
                argumentCount = Indices.ADAPTIVE_ANONYMOUS_FRAGMENT_ARGUMENT_COUNT
            ).apply {
                putValueArgument(Indices.ADAPTIVE_FRAGMENT_STATE_SIZE, irConst(armCall.arguments.count()))
                putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY, irGetFragmentFactory(buildFun))
            }
        }

    fun irGetFragmentFactory(buildFun: IrSimpleFunction): IrExpression {

        val valueParameter = (armCall.irCall.dispatchReceiver as IrGetValue).symbol.owner as IrValueParameterImpl
        val argumentIndex = valueParameter.index

        val getState = irCall(
            irClass.getPropertyGetter(Strings.STATE)!!,
            dispatchReceiver = irGet(buildFun.dispatchReceiverParameter!!)
        )

        val getStateVariable = irCall(
            pluginContext.arrayGet,
            dispatchReceiver = getState,
            args = arrayOf(irConst(argumentIndex))
        )

        return irImplicitAs(
            pluginContext.adaptiveFragmentFactoryClass.defaultType,
            getStateVariable
        )
    }

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrBlock {
        val fragmentParameter = patchFun.valueParameters.first()

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->
                for (argument in armCall.arguments) {
                    argument.toPatchExpression(
                        this,
                        patchFun,
                        armCall.closure,
                        fragmentParameter,
                        closureMask
                    )?.also {
                        block.statements += it
                    }
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

        val transformClosure =
            originalClosure + functionToTransform.valueParameters.mapIndexed { indexInState, parameter ->
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