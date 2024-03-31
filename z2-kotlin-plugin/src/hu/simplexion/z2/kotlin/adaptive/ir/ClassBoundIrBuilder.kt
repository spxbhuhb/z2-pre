package hu.simplexion.z2.kotlin.adaptive.ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.StateAccessTransform
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irSetField
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.util.OperatorNameConventions

open class ClassBoundIrBuilder(
    val pluginContext: AdaptivePluginContext
) {

    constructor(parent: ClassBoundIrBuilder) : this(parent.pluginContext) {
        this.irClass = parent.irClass
        this.airClass = parent.airClass
    }

    constructor(context: AdaptivePluginContext, airClass: AirClass) : this(context) {
        this.irClass = airClass.irClass
        this.airClass = airClass
    }

    lateinit var irClass: IrClass

    lateinit var airClass: AirClass

    val irContext
        get() = pluginContext.irContext

    val irFactory
        get() = irContext.irFactory

    val irBuiltIns
        get() = irContext.irBuiltIns

    val classBoundBridgeType: IrTypeParameter
        get() = irClass.typeParameters.first()

    val classBoundFragmentType: IrType
        get() = pluginContext.adaptiveFragmentClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundNullableFragmentType: IrType
        get() = classBoundFragmentType.makeNullable()

    val classBoundClosureType: IrType
        get() = pluginContext.adaptiveClosureClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundSupportFunctionType: IrType
        get() = pluginContext.adaptiveSupportFunctionClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundFragmentFactoryType: IrType
        get() = pluginContext.adaptiveFragmentFactoryClass.typeWith(classBoundBridgeType.defaultType)

    val classBoundAdapterType: IrType
        get() = pluginContext.adaptiveAdapterClass.typeWith(classBoundBridgeType.defaultType)

    // FIXME check uses of irThisReceiver
    fun irThisReceiver(): IrExpression =
        IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irClass.thisReceiver !!.symbol)

    // --------------------------------------------------------------------------------------------------------
    // Build, patch and invoke helpers
    // --------------------------------------------------------------------------------------------------------

    fun irConstructorCallFromBuild(target: FqName): IrExpression {
        val buildFun = airClass.build
        val classSymbol = pluginContext.airClasses[target]?.irClass?.symbol ?: pluginContext.classSymbol(target)

        val constructorCall =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                classSymbol.defaultType,
                classSymbol.constructors.single(),
                typeArgumentsCount = 1, // bridge type
                constructorTypeArgumentsCount = 0,
                Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT
            )

        constructorCall.putTypeArgument(Indices.ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)

        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGetValue(airClass.adapter, irGet(buildFun.dispatchReceiverParameter !!)))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(buildFun.valueParameters[Indices.BUILD_PARENT]))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]))

        return constructorCall
    }

    fun irFragmentFactoryFromPatch(index: Int): IrExpression {
        val patchFun = airClass.patchDescendant

        val constructorCall =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                classBoundFragmentType,
                pluginContext.adaptiveFragmentFactoryConstructor,
                typeArgumentsCount = 1, // bridge type
                constructorTypeArgumentsCount = 0,
                Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT
            )

        constructorCall.putTypeArgument(Indices.ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)

        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT, irGet(patchFun.dispatchReceiverParameter !!))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX, irConst(index))

        return constructorCall
    }

    fun irSetDescendantStateVariable(stateVariableIndex: Int, value: IrExpression) =
        IrCallImpl(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0,
            valueArgumentsCount = Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also { call ->

            call.dispatchReceiver = irGet(airClass.patchDescendant.valueParameters.first())

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                value
            )
        }

    fun irSetInternalStateVariable(stateVariableIndex: Int, value: IrExpression) =
        IrCallImpl(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0,
            valueArgumentsCount = Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also { call ->

            call.dispatchReceiver = irGet(airClass.patchInternal.dispatchReceiverParameter!!)

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                value
            )
        }

    fun IrExpression.transformStateAccess(closure: ArmClosure, irGetFragment: () -> IrExpression): IrExpression =
        transform(StateAccessTransform(this@ClassBoundIrBuilder, closure, irGetFragment), null)

    fun IrStatement.transformStateAccess(closure: ArmClosure, irGetFragment: () -> IrExpression): IrStatement =
        transform(StateAccessTransform(this@ClassBoundIrBuilder, closure, irGetFragment), null) as IrStatement

    // --------------------------------------------------------------------------------------------------------
    // Properties
    // --------------------------------------------------------------------------------------------------------

    /**
     * Adds a property to [irClass].
     */
    fun addIrProperty(
        inName: Name,
        inType: IrType,
        inIsVar: Boolean,
        inInitializer: IrExpression? = null,
        overridden: List<IrPropertySymbol>? = null
    ): IrProperty {

        val irField = irFactory.buildField {
            name = inName
            type = inType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = irClass
            initializer = inInitializer?.let { irFactory.createExpressionBody(it) }
        }

        val irProperty = irClass.addProperty {
            name = inName
            isVar = true
        }.apply {
            parent = irClass
            backingField = irField
            overridden?.let { overriddenSymbols = it }
            addDefaultGetter(irClass, irBuiltIns)
        }

        if (inIsVar) {
            irProperty.addDefaultSetter(irField)
        }

        return irProperty
    }

    fun IrProperty.addDefaultSetter(irField: IrField) {

        setter = irFactory.buildFun {

            origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
            name = Name.identifier("set-" + irField.name.identifier)
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.FINAL
            returnType = irBuiltIns.unitType

        }.also {

            it.parent = parent
            it.correspondingPropertySymbol = symbol

            val receiver = it.addDispatchReceiver {
                type = parentAsClass.defaultType
            }

            val value = it.addValueParameter {
                name = Name.identifier("set-?")
                type = irField.type
            }

            it.body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {
                + irSetField(
                    receiver = irGet(receiver),
                    field = irField,
                    value = irGet(value)
                )
            }
        }
    }

    fun IrProperty.irSetField(value: IrExpression, receiver: IrExpression): IrSetFieldImpl {
        return IrSetFieldImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            backingField !!.symbol,
            receiver,
            value,
            irBuiltIns.unitType
        )
    }

    fun irGetValue(irProperty: IrProperty, receiver: IrExpression?): IrCall =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irProperty.getter !!.returnType,
            irProperty.getter !!.symbol,
            0, 0,
            origin = IrStatementOrigin.GET_PROPERTY
        ).apply {
            dispatchReceiver = receiver
        }

    fun irSetValue(irProperty: IrProperty, value: IrExpression, receiver: IrExpression?): IrCall =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irProperty.backingField !!.type,
            irProperty.setter !!.symbol,
            0, 1
        ).apply {
            dispatchReceiver = receiver
            putValueArgument(0, value)
        }

    // --------------------------------------------------------------------------------------------------------
    // IR Basics
    // --------------------------------------------------------------------------------------------------------

    fun irConst(value: Long): IrConst<Long> = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.longType,
        IrConstKind.Long,
        value
    )

    fun irConst(value: Int): IrConst<Int> = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.intType,
        IrConstKind.Int,
        value
    )

    fun irConst(value: String): IrConst<String> = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.stringType,
        IrConstKind.String,
        value
    )

    fun irConst(value: Boolean) = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.booleanType,
        IrConstKind.Boolean,
        value
    )

    fun irNull() = IrConstImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        irContext.irBuiltIns.anyNType,
        IrConstKind.Null,
        null
    )

    fun irGet(type: IrType, symbol: IrValueSymbol, origin: IrStatementOrigin?): IrExpression {
        return IrGetValueImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            type,
            symbol,
            origin
        )
    }

    fun irGet(variable: IrValueDeclaration, origin: IrStatementOrigin? = null): IrExpression {
        return irGet(variable.type, variable.symbol, origin)
    }

    fun irIf(condition: IrExpression, body: IrExpression): IrExpression {
        return IrIfThenElseImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            irContext.irBuiltIns.unitType,
            origin = IrStatementOrigin.IF
        ).also {
            it.branches.add(
                IrBranchImpl(condition, body)
            )
        }
    }

    fun irImplicitAs(toType: IrType, argument: IrExpression): IrTypeOperatorCallImpl {
        return IrTypeOperatorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            toType,
            IrTypeOperator.IMPLICIT_CAST,
            toType,
            argument
        )
    }

    // --------------------------------------------------------------------------------------------------------
    // Logic
    // --------------------------------------------------------------------------------------------------------

    fun irAnd(lhs: IrExpression, rhs: IrExpression): IrCallImpl {
        return irCall(
            lhs.type.binaryOperator(OperatorNameConventions.AND, rhs.type),
            null,
            lhs,
            null,
            rhs
        )
    }

    fun irOr(lhs: IrExpression, rhs: IrExpression): IrCallImpl {
        val int = irContext.irBuiltIns.intType
        return irCall(
            int.binaryOperator(OperatorNameConventions.OR, int),
            null,
            lhs,
            null,
            rhs
        )
    }

    fun irEqual(lhs: IrExpression, rhs: IrExpression): IrExpression {
        return irCall(
            this.irContext.irBuiltIns.eqeqSymbol,
            null,
            null,
            null,
            lhs,
            rhs
        )
    }

    fun irNot(value: IrExpression): IrExpression {
        return irCall(
            irContext.irBuiltIns.booleanNotSymbol,
            dispatchReceiver = value
        )
    }

    fun irNotEqual(lhs: IrExpression, rhs: IrExpression): IrExpression {
        return irNot(irEqual(lhs, rhs))
    }

    fun irOrOr(lhs: IrExpression, rhs: IrExpression): IrExpression {
        return IrWhenImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            origin = IrStatementOrigin.OROR,
            type = irContext.irBuiltIns.booleanType,
            branches = listOf(
                IrBranchImpl(
                    UNDEFINED_OFFSET,
                    UNDEFINED_OFFSET,
                    condition = lhs,
                    result = irConst(true)
                ),
                IrElseBranchImpl(
                    UNDEFINED_OFFSET,
                    UNDEFINED_OFFSET,
                    condition = irConst(true),
                    result = rhs
                )
            )
        )
    }

    // --------------------------------------------------------------------------------------------------------
    // Operators
    // --------------------------------------------------------------------------------------------------------

    fun IrType.binaryOperator(name: Name, paramType: IrType): IrFunctionSymbol =
        irContext.symbols.getBinaryOperator(name, this, paramType)

    // --------------------------------------------------------------------------------------------------------
    // Calls
    // --------------------------------------------------------------------------------------------------------

    fun irCall(
        symbol: IrFunctionSymbol,
        origin: IrStatementOrigin? = null,
        dispatchReceiver: IrExpression? = null,
        extensionReceiver: IrExpression? = null,
        vararg args: IrExpression
    ): IrCallImpl {
        return IrCallImpl(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            symbol.owner.returnType,
            symbol as IrSimpleFunctionSymbol,
            symbol.owner.typeParameters.size,
            symbol.owner.valueParameters.size,
            origin
        ).also {
            if (dispatchReceiver != null) it.dispatchReceiver = dispatchReceiver
            if (extensionReceiver != null) it.extensionReceiver = extensionReceiver
            args.forEachIndexed { index, arg ->
                it.putValueArgument(index, arg)
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // Trace
    // --------------------------------------------------------------------------------------------------------

//    fun irTrace(point: String, parameters: List<IrExpression>): IrStatement {
//        return irTrace(irThisReceiver(), point, parameters)
//    }
//
//    fun irTrace(function: IrFunction, point: String, parameters: List<IrExpression>): IrStatement {
//        return irTrace(irGet(function.dispatchReceiverParameter !!), point, parameters)
//    }
//
//    fun irTrace(fragment: IrExpression, point: String, parameters: List<IrExpression>): IrStatement {
//        return irTraceDirect(irGetValue(airClass.adapter, fragment), point, parameters)
//    }
//
//    /**
//     * @param dispatchReceiver The `AdaptiveAdapter` instance to use for the trace.
//     */
//    fun irTraceDirect(dispatchReceiver: IrExpression, point: String, parameters: List<IrExpression>): IrStatement {
//        return IrCallImpl(
//            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
//            irBuiltIns.unitType,
//            pluginContext.adaptiveAdapterTrace,
//            typeArgumentsCount = 0,
//            Indices.ADAPTIVE_TRACE_ARGUMENT_COUNT,
//        ).also {
//            it.dispatchReceiver = dispatchReceiver
//            it.putValueArgument(Indices.ADAPTIVE_TRACE_ARGUMENT_NAME, irConst(irClass.name.identifier))
//            it.putValueArgument(Indices.ADAPTIVE_TRACE_ARGUMENT_POINT, irConst(point))
//            it.putValueArgument(Indices.ADAPTIVE_TRACE_ARGUMENT_DATA, buildTraceVarArg(parameters))
//        }
//    }
//
//    fun buildTraceVarArg(parameters: List<IrExpression>): IrExpression {
//        return IrVarargImpl(
//            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
//            irBuiltIns.arrayClass.typeWith(irBuiltIns.anyNType),
//            pluginContext.adaptiveFragmentType,
//        ).also { vararg ->
//            parameters.forEach {
//                vararg.addElement(it)
//            }
//        }
//    }

    // --------------------------------------------------------------------------------------------------------
    // Misc
    // --------------------------------------------------------------------------------------------------------

    val String.function: IrFunction
        get() = irClass.declarations.first { it is IrFunction && it.name.asString() == this } as IrFunction

    val String.name: Name
        get() = Name.identifier(this)

}