/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.services.*
import hu.simplexion.z2.kotlin.services.ir.proto.ProtoCache
import hu.simplexion.z2.kotlin.services.ir.proto.ProtoEnum
import hu.simplexion.z2.kotlin.services.ir.util.ServiceFunctionCache
import hu.simplexion.z2.kotlin.util.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class ServicesPluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    override val runtimePackage = RUNTIME_PACKAGE

    val serviceClass = SERVICE_CLASS.runtimeClass()
    val serviceType = serviceClass.defaultType
    val serviceName = serviceClass.owner.properties.first { it.name == Names.SERVICE_NAME_PROPERTY }

    val serviceImplClass = SERVICE_IMPL_CLASS.runtimeClass()
    val serviceImplType = SERVICE_IMPL_CLASS.runtimeClass().defaultType
    val serviceImplNewInstance = serviceImplClass.owner.functions.single { it.name.identifier == SERVICE_IMPL_NEW_INSTANCE }.symbol

    val serviceCallTransportClass = SERVICE_CALL_TRANSPORT_CLASS.runtimeClass(TRANSPORT_PACKAGE)
    val callFunction = serviceCallTransportClass.functionByName(CALL_FUNCTION)

    val defaultServiceCallTransport = checkNotNull(
        irContext
            .referenceProperties(CallableId(FqName(RUNTIME_PACKAGE), Name.identifier(DEFAULT_SERVICE_CALL_TRANSPORT)))
            .first().owner.getter?.symbol
    ) { "Missing $GLOBALS_CLASS, is the plugin added to gradle?" }

    val getService = checkNotNull(
        irContext
            .referenceFunctions(CallableId(FqName(RUNTIME_PACKAGE), Name.identifier(GET_SERVICE)))
            .first()
    ) { "Missing $GLOBALS_CLASS, is the plugin added to gradle?" }

    val serviceContextType = SERVICE_CONTEXT_CLASS.runtimeClass().defaultType

    val noTransformAnnotation = FqName("hu.simplexion.z2.services.NoServiceTransform")

    val serviceFunctionCache = ServiceFunctionCache()
    val protoCache = ProtoCache(this)
    val protoEnum = ProtoEnum(this)

}