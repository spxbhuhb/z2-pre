package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.AdaptivePluginKey
import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Names
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.*
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.IrTypeParameterImpl
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrTypeParameterSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.types.Variance

class ArmClassBuilder(
    context: AdaptivePluginContext,
    val armClass: ArmClass
) : ClassBoundIrBuilder(context) {

    // --------------------------------------------------------------------------------------------------------
    // First step of class generation: everything without generated function bodies
    // --------------------------------------------------------------------------------------------------------

    fun buildIrClassWithoutGenBodies() {

        val originalFunction = armClass.originalFunction

        irClass = pluginContext.irContext.irFactory.buildClass {
            startOffset = originalFunction.startOffset
            endOffset = originalFunction.endOffset
            origin = AdaptivePluginKey.origin
            name = armClass.name
            kind = ClassKind.CLASS
            visibility = originalFunction.visibility
            modality = Modality.OPEN
        }

        typeParameters()

        irClass.parent = originalFunction.file
        irClass.superTypes = listOf(pluginContext.adaptiveFragmentClass.typeWith(irClass.typeParameters.first().defaultType))
        irClass.metadata = armClass.originalFunction.metadata

        thisReceiver()

        // these have to be before addFakeOverrides()

        constructor()
        initializer()

        genBuild()
        genPatchDescendant()
        genInvoke()
        genPatchInternal()

        // this has to be before AirClass() (adds overridden properties)

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        pluginContext.irClasses[armClass.fqName] = irClass
    }

    // --------------------------------------------------------------------------------------------------------
    // Declaration and initialization
    // --------------------------------------------------------------------------------------------------------

    private fun typeParameters() {
        irClass.typeParameters = listOf(
            IrTypeParameterImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                IrDeclarationOrigin.BRIDGE_SPECIAL,
                IrTypeParameterSymbolImpl(),
                Names.BT,
                index = 0,
                isReified = false,
                variance = Variance.IN_VARIANCE,
                factory = irFactory
            ).also {
                it.parent = irClass
                it.superTypes = listOf(irBuiltIns.anyNType)
            }
        )
    }

    private fun thisReceiver(): IrValueParameter =

        irFactory.createValueParameter(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            IrDeclarationOrigin.INSTANCE_RECEIVER,
            IrValueParameterSymbolImpl(),
            SpecialNames.THIS,
            UNDEFINED_PARAMETER_INDEX,
            IrSimpleTypeImpl(irClass.symbol, false, emptyList(), emptyList()),
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false
        ).also {
            it.parent = irClass
            irClass.thisReceiver = it
        }

    private fun constructor(): IrConstructor =

        irClass.addConstructor {
            isPrimary = true
            returnType = irClass.typeWith()
        }.apply {
            parent = irClass

            val adapter = addValueParameter {
                name = Names.ADAPTER
                type = classBoundAdapterType
            }

            val parent = addValueParameter {
                name = Names.PARENT
                type = classBoundFragmentType.makeNullable()
            }

            val index = addValueParameter {
                name = Names.INDEX
                type = irBuiltIns.intType
            }

            body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

                statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                    SYNTHETIC_OFFSET,
                    SYNTHETIC_OFFSET,
                    pluginContext.adaptiveFragmentClass.typeWith(classBoundFragmentType),
                    pluginContext.adaptiveFragmentClass.constructors.first(),
                    typeArgumentsCount = 1,
                    valueArgumentsCount = Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT
                ).apply {
                    putTypeArgument(0, classBoundFragmentType)
                    putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGet(adapter))
                    putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(parent))
                    putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irGet(index))
                    putValueArgument(Indices.ADAPTIVE_FRAGMENT_STATE_SIZE, irConst(armClass.stateVariables.size))
                }

                statements += IrInstanceInitializerCallImpl(
                    SYNTHETIC_OFFSET,
                    SYNTHETIC_OFFSET,
                    irClass.symbol,
                    irBuiltIns.unitType
                )
            }
        }

    private fun initializer(): IrAnonymousInitializer =
        irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        ).also { initFun ->
            initFun.parent = irClass
            irClass.declarations += initFun

            initFun.body = DeclarationIrBuilder(irContext, initFun.symbol).irBlockBody {

            }
        }

    // --------------------------------------------------------------------------------------------------------
    // Child fragment handling functions
    // --------------------------------------------------------------------------------------------------------

    fun function(
        inName: Name,
        inReturnType: IrType,
        overridden: IrSimpleFunctionSymbol,
        vararg parameters: Pair<Name, IrType>
    ): IrSimpleFunction =
        irFactory.buildFun {
            name = inName
            returnType = inReturnType
            modality = Modality.OPEN
        }.also { function ->

            function.overriddenSymbols = listOf(overridden)

            function.addDispatchReceiver {
                type = irClass.typeWith(irClass.typeParameters.first().defaultType)
            }

            for (parameter in parameters) {
                function.addValueParameter {
                    name = parameter.first
                    type = parameter.second
                }
            }

            function.parent = irClass
            irClass.declarations += function
        }

    /**
     * `genBuild(parent : AdaptiveFragment<BT>, declarationIndex : Int) : AdaptiveFragment<BT>`
     */
    fun genBuild(): IrSimpleFunction =
        function(
            Names.GEN_BUILD,
            classBoundFragmentType,
            pluginContext.genBuild,
            Names.PARENT to classBoundFragmentType,
            Names.DECLARATION_INDEX to irBuiltIns.intType
        )

    /**
     * `genDatchDescendant(fragment : AdaptiveFragment<BT>)`
     */
    fun genPatchDescendant(): IrSimpleFunction =
        function(
            Names.GEN_PATCH_DESCENDANT,
            irBuiltIns.unitType,
            pluginContext.genPatchDescendant,
            Names.FRAGMENT to classBoundFragmentType
        )

    /**
     * `genInvoke(supportFunction: AdaptiveSupportFunction<BT>, arguments : Array<out Any?>) : Any?`
     */
    fun genInvoke(): IrSimpleFunction =
        function(
            Names.GEN_INVOKE,
            irBuiltIns.anyNType,
            pluginContext.genInvoke,
            Names.SUPPORT_FUNCTION to classBoundSupportFunctionType,
            Names.CALLING_FRAGMENT to classBoundFragmentType
        ).apply {
            addValueParameter {
                name = Names.ARGUMENTS
                type = irBuiltIns.arrayClass.typeWith(irBuiltIns.anyNType)
            }
        }

    /**
     * `genPatchInternal()`
     */
    fun genPatchInternal(): IrSimpleFunction =
        function(
            Names.GEN_PATCH_INTERNAL,
            irBuiltIns.unitType,
            pluginContext.genPatchInternal
        )

    // --------------------------------------------------------------------------------------------------------
    // Second step of class generation: generated function bodies
    // --------------------------------------------------------------------------------------------------------

    fun buildGenFunctionBodies() {
        genBuildBody()
        genPatchDescendantBody()
        genInvokeBody()
        genPatchInternalBody()
    }

    // ---------------------------------------------------------------------------
    // Build
    // ---------------------------------------------------------------------------

    fun genBuildBody() {
        val buildFun = irClass.getSimpleFunction(Strings.GEN_BUILD) !!.owner

        buildFun.body = DeclarationIrBuilder(irContext, buildFun.symbol).irBlockBody {
            val fragment = irTemporary(genBuildWhen(buildFun))

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

    private fun genBuildWhen(buildFun: IrSimpleFunction): IrExpression =

        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundFragmentType,
            IrStatementOrigin.WHEN
        ).apply {

            armClass.rendering.forEach {
                branches += genBuildWhenBranch(buildFun, it)
            }

            branches += irInvalidIndexBranch(buildFun, irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]))
        }

    private fun genBuildWhenBranch(buildFun: IrSimpleFunction, renderingStatement: ArmRenderingStatement) =

        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]),
                irConst(renderingStatement.index)
            ),
            renderingStatement.branchBuilder(this@ArmClassBuilder).genBuildConstructorCall(buildFun)
        )

    // ---------------------------------------------------------------------------
    // Patch Descendants
    // ---------------------------------------------------------------------------

    fun genPatchDescendantBody() {
        val patchFun = irClass.getSimpleFunction(Strings.GEN_PATCH_DESCENDANT) !!.owner

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

            + genPatchDescendantWhen(patchFun, fragmentIndex, closureMask)
        }
    }

    private fun genPatchDescendantWhen(patchFun: IrSimpleFunction, fragmentIndex: IrVariable, closureMask: IrVariable): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            IrStatementOrigin.WHEN
        ).apply {

            armClass.rendering.forEach {
                branches += genPatchDescendantBranch(patchFun, it, fragmentIndex, closureMask)
            }

            branches += irInvalidIndexBranch(patchFun, irGet(fragmentIndex))
        }

    private fun genPatchDescendantBranch(patchFun: IrSimpleFunction, branch: ArmRenderingStatement, fragmentIndex: IrVariable, closureMask: IrVariable) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(fragmentIndex),
                irConst(branch.index)
            ),
            branch.branchBuilder(this).genPatchDescendantBranch(patchFun, closureMask)
        )

    // ---------------------------------------------------------------------------
    // Invoke
    // ---------------------------------------------------------------------------

    fun genInvokeBody() {
        val invokeFun = irClass.getSimpleFunction(Strings.GEN_INVOKE) !!.owner

        invokeFun.body = DeclarationIrBuilder(irContext, invokeFun.symbol).irBlockBody {

            val supportFunctionIndex = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.adaptiveSupportFunctionIndex,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(invokeFun.valueParameters[Indices.INVOKE_SUPPORT_FUNCTION])
                }
            )

            val callingFragment = irTemporary(
                irGet(invokeFun.valueParameters[Indices.INVOKE_CALLING_FRAGMENT])
            )

            val arguments = irTemporary(
                irGet(invokeFun.valueParameters[Indices.INVOKE_ARGUMENTS])
            )

            + genInvokeWhen(invokeFun, supportFunctionIndex, callingFragment, arguments)
        }
    }

    private fun genInvokeWhen(invokeFun: IrSimpleFunction, supportFunctionIndex: IrVariable, callingFragment: IrVariable, arguments: IrVariable): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            IrStatementOrigin.WHEN
        ).apply {

            armClass.rendering.forEach { branch ->
                if (branch.hasInvokeBranch) {
                    branches += branch.branchBuilder(this@ArmClassBuilder).genInvokeBranches(invokeFun, supportFunctionIndex, callingFragment, arguments)
                }
            }

            branches += irInvalidIndexBranch(invokeFun, irGet(supportFunctionIndex))
        }

    // ---------------------------------------------------------------------------
    // Patch Internal
    // ---------------------------------------------------------------------------

    fun genPatchInternalBody() {
        val patchFun = irClass.getSimpleFunction(Strings.GEN_PATCH_INTERNAL) !!.owner

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            val closureMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.getThisClosureDirtyMask,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)
                }
            )

            armClass.stateDefinitionStatements.forEach {
                // FIXME casting a statement into an expression in internal patch
                val originalExpression = when (it) {
                    is ArmInternalStateVariable -> it.irVariable.initializer !!
                    is ArmDefaultValueStatement -> it.defaultValue
                    else -> it.irStatement as IrExpression
                }

                val transformedExpression = originalExpression.transformThisStateAccess(armClass.stateVariables) { irGet(patchFun.dispatchReceiverParameter !!) }

                + genPatchInternalExpression(
                    patchFun,
                    closureMask,
                    when (it) {
                        is ArmInternalStateVariable -> irSetStateVariable(patchFun, it.indexInState, transformedExpression)
                        is ArmDefaultValueStatement -> irSetStateVariable(patchFun, it.indexInState, transformedExpression)
                        else -> transformedExpression
                    },
                    it.dependencies
                )
            }

            + irReturn(irUnit())
        }
    }

    fun genPatchInternalExpression(patchFun: IrSimpleFunction, closureMask: IrVariable, expression: IrExpression, dependencies: ArmDependencies): IrExpression =
        irIf(
            genPatchInternalCondition(patchFun, closureMask, dependencies),
            expression
        )

    fun genPatchInternalCondition(patchFun: IrSimpleFunction, closureMask: IrVariable, dependencies: ArmDependencies): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!),
            args = arrayOf(
                irGet(closureMask),
                dependencies.toDirtyMask()
            )
        )

    // ---------------------------------------------------------------------------
    // Common
    // ---------------------------------------------------------------------------

    private fun irInvalidIndexBranch(fromFun: IrSimpleFunction, getIndex: IrExpression) =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.nothingType,
                irClass.getSimpleFunction(Strings.INVALID_INDEX) !!,
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