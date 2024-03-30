package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.AdaptivePluginKey
import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.Names
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.IrTypeParameterImpl
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrDelegatingConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrTypeParameterSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.addFakeOverrides
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.types.Variance

class ArmClass2Air(
    context: AdaptivePluginContext,
    val armClass: ArmClass
) : ClassBoundIrBuilder(context) {

    fun toAir(): AirClass {

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
        irClass.superTypes = listOf(pluginContext.adaptiveGeneratedFragmentClass.typeWith(irClass.typeParameters.first().defaultType))
        irClass.metadata = armClass.originalFunction.metadata

        thisReceiver()

        // these have to be before addFakeOverrides()

        val constructor = constructor()
        val initializer = initializer()
        val build = build()
        val patchExternal = patchExternal()
        val invoke = invoke()

        // this has to be before AirClass() (adds overridden properties)

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        airClass = AirClass(
            armClass,
            irClass,
            constructor,
            initializer,
            build,
            patchExternal,
            invoke
        )

        airClass.stateVariableList = armClass.stateVariables.map { it.toAir(this@ArmClass2Air) }
        airClass.stateVariableMap = airClass.stateVariableList.associateBy { it.name }


        armClass.rendering.forEach { it.toAir(this) } // adds build, patch and invoke branches

        return airClass
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
                    pluginContext.adaptiveGeneratedFragmentClass.typeWith(classBoundFragmentType),
                    pluginContext.adaptiveGeneratedFragmentClass.constructors.first(),
                    typeArgumentsCount = 1,
                    valueArgumentsCount = Indices.ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT
                ).apply {
                    putTypeArgument(0, classBoundFragmentType)
                    putValueArgument(Indices.ADAPTIVE_GENERATED_FRAGMENT_ADAPTER, irGet(adapter))
                    putValueArgument(Indices.ADAPTIVE_GENERATED_FRAGMENT_PARENT, irGet(parent))
                    putValueArgument(Indices.ADAPTIVE_GENERATED_FRAGMENT_INDEX, irGet(index))
                    putValueArgument(Indices.ADAPTIVE_GENERATED_FRAGMENT_STATE_SIZE, irConst(armClass.stateVariables.size))
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
                armClass.originalInitializationStatements.forEach { statement ->
                    + statement.transformStateAccess(armClass.stateVariables) { irGet(irClass.thisReceiver !!) }
                }
            }
        }

    /**
     * Adds a constructor parameter and a property with the same name. The property
     * is initialized from the constructor parameter.
     */
    fun addPropertyWithConstructorParameter(
        inName: Name,
        inType: IrType,
        inIsVar: Boolean = false,
        overridden: List<IrPropertySymbol>? = null,
        inVarargElementType: IrType? = null
    ): IrProperty =

        with(irClass.constructors.first()) {

            addValueParameter {
                name = inName
                type = inType
                varargElementType = inVarargElementType
            }.let {
                addIrProperty(
                    inName,
                    inType,
                    inIsVar,
                    irGet(it, origin = IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER),
                    overridden
                )
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
     * Defines a `build(parent : AdaptiveFragment<BT>, declarationIndex : Int) : AdaptiveFragment<BT>`
     */
    fun build(): IrSimpleFunction =
        function(
            Names.BUILD,
            classBoundFragmentType,
            pluginContext.build,
            Names.PARENT to classBoundFragmentType,
            Names.DECLARATION_INDEX to irBuiltIns.intType
        )

    /**
     * Defines a `patchExternal(fragment : AdaptiveFragment<BT>)`
     */
    fun patchExternal(): IrSimpleFunction =
        function(
            Names.PATCH_EXTERNAL,
            irBuiltIns.unitType,
            pluginContext.patchExternal,
            Names.FRAGMENT to classBoundFragmentType
        )

    /**
     * Defines a `invoke(supportFunction: AdaptiveSupportFunction<BT>, arguments : Array<out Any?>) : Any?`
     */
    fun invoke(): IrSimpleFunction =
        function(
            Names.INVOKE,
            irBuiltIns.anyNType,
            pluginContext.invoke,
            Names.SUPPORT_FUNCTION to classBoundSupportFunctionType
        ).also {
            it.addValueParameter {
                name = Names.ARGUMENTS
                type = irBuiltIns.arrayClass.typeWith(irBuiltIns.anyNType)
                varargElementType = irBuiltIns.anyNType
            }
        }
}