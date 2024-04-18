/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.consumer

import hu.simplexion.z2.kotlin.services.*
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import hu.simplexion.z2.kotlin.services.ir.proto.ProtoMessageBuilderIrBuilder
import hu.simplexion.z2.kotlin.services.ir.proto.ProtoOneIrBuilder
import hu.simplexion.z2.kotlin.services.ir.util.FunctionSignature
import hu.simplexion.z2.kotlin.services.ir.util.IrClassBaseBuilder
import hu.simplexion.z2.kotlin.services.ir.util.ServiceBuilder
import hu.simplexion.z2.kotlin.util.property
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid

/**
 * Add initializers and function bodies to the consumer class.
 */
class ConsumerClassTransform(
    override val pluginContext: ServicesPluginContext,
    val interfaceClass: IrClass
) : IrElementVisitorVoid, ServiceBuilder, IrClassBaseBuilder {

    val consumerClass = interfaceClass.declarations.let {
        requireNotNull(interfaceClass.declarations.firstOrNull { it is IrClass && it.name == interfaceClass.name.serviceConsumerName }) {
            "missing consumer class for ${interfaceClass.classId}"
        } as IrClass
    }

    override val overriddenServiceFunctions = mutableListOf<IrSimpleFunctionSymbol>()

    override val serviceNames = mutableListOf<String>()

    override lateinit var serviceNameGetter: IrSimpleFunctionSymbol

    fun build() {
        collectServiceFunctions(interfaceClass)

        addServiceNameInitializer()
        addServiceCallTransportInitializer()

        for (serviceFunction in consumerClass.functions) {
            transformServiceFunction(serviceFunction)
        }
    }

    private fun addServiceNameInitializer() {
        val property = consumerClass.property(Names.SERVICE_NAME_PROPERTY)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(irConst(interfaceClass.kotlinFqName.asString()))
        serviceNameGetter = property.getter!!.symbol
    }

    private fun addServiceCallTransportInitializer() {
        val property = consumerClass.property(Names.SERVICE_CALL_TRANSPORT_PROPERTY)
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(irNull())
    }

    private fun transformServiceFunction(function: IrSimpleFunction) {
        val origin = function.origin as? IrDeclarationOrigin.GeneratedByPlugin

        when {
            function.isFakeOverride && function.isSuspend -> transformFakeOverrideFunction(function)
            origin?.pluginKey == ServicesPluginKey -> transformDeclaredFunction(function)
        }
    }

    private fun transformFakeOverrideFunction(function: IrSimpleFunction) {
        function.isFakeOverride = false
        function.origin = IrDeclarationOrigin.GeneratedByPlugin(ServicesPluginKey)
        function.modality = Modality.FINAL
        function.addDispatchReceiver {// replace the interface in the dispatcher with the class
            type = consumerClass.defaultType
        }
        transformDeclaredFunction(function)
    }

    fun transformDeclaredFunction(function: IrSimpleFunction) {
        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            +irReturn(
                irCall(
                    pluginContext.callFunction,
                    dispatchReceiver = getServiceCallTransport(irGet(function.dispatchReceiverParameter!!))
                ).also {
                    it.type = function.returnType
                    it.putTypeArgument(CALL_TYPE_INDEX, function.returnType)
                    it.putValueArgument(CALL_SERVICE_NAME_INDEX, getServiceName(function))
                    it.putValueArgument(CALL_FUN_NAME_INDEX, irConst(FunctionSignature(pluginContext, function).signature()))
                    it.putValueArgument(CALL_PAYLOAD_INDEX, buildPayload(function))
                    it.putValueArgument(CALL_DECODER_INDEX, ProtoOneIrBuilder(pluginContext).getDecoder(function.returnType))
                }
            )
        }
    }

    fun getServiceCallTransport(dispatchReceiver: IrExpression): IrCallImpl =
        irCall(
            consumerClass.functions.firstOrNull { it.name == Names.SERVICE_CALL_TRANSPORT_OR_DEFAULT }!!.symbol,
            dispatchReceiver = dispatchReceiver
        )

    fun buildPayload(function: IrSimpleFunction): IrExpression {
        val protoBuilder = ProtoMessageBuilderIrBuilder(pluginContext)

        for (valueParameter in function.valueParameters) {
            protoBuilder.next(valueParameter)
            check(protoBuilder.valid) {
                "unsupported type: ${valueParameter.symbol} function: ${function.symbol}\n" +
                    valueParameter.type.getClass()?.companionObject()?.dump() + "\n" +
                    valueParameter.type.getClass()?.dump()
            }
        }

        return irCall(
            pluginContext.protoCache.protoMessageBuilderPack,
            dispatchReceiver = protoBuilder.current
        )
    }

}
