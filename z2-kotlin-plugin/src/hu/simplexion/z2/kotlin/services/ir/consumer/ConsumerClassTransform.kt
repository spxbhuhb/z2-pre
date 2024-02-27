/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.consumer

import hu.simplexion.z2.kotlin.services.ServicesPluginKey
import hu.simplexion.z2.kotlin.services.ir.*
import hu.simplexion.z2.kotlin.services.ir.proto.ProtoMessageBuilderIrBuilder
import hu.simplexion.z2.kotlin.services.ir.proto.ProtoOneIrBuilder
import hu.simplexion.z2.kotlin.services.ir.util.FunctionSignature
import hu.simplexion.z2.kotlin.services.ir.util.IrClassBaseBuilder
import hu.simplexion.z2.kotlin.services.ir.util.ServiceBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
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

        for (serviceFunction in consumerClass.functions) {
            transformServiceFunction(serviceFunction)
        }
    }

    private fun addServiceNameInitializer() {
        val property = requireNotNull(consumerClass.properties.firstOrNull { it.name == SERVICE_NAME_PROPERTY.asName })
        val backingField = requireNotNull(property.backingField)

        backingField.initializer = irFactory.createExpressionBody(irConst(interfaceClass.kotlinFqName.asString()))
        serviceNameGetter = property.getter !!.symbol
    }

    private fun transformServiceFunction(function: IrSimpleFunction) {
        val origin = function.origin as? IrDeclarationOrigin.GeneratedByPlugin ?: return
        if (origin.pluginKey != ServicesPluginKey) return

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            + irReturn(
                irCall(
                    pluginContext.callFunction,
                    dispatchReceiver = getServiceTransport()
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

    fun getServiceTransport(): IrCallImpl =
        irCall(
            pluginContext.defaultServiceCallTransport,
            IrStatementOrigin.GET_PROPERTY
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
