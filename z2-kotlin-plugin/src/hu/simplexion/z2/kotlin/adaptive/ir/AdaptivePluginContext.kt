/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.adaptive.CallableIds
import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmEntryPoint
import hu.simplexion.z2.kotlin.util.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName

class AdaptivePluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    override val runtimePackage = Strings.RUNTIME_PACKAGE

    val irBuiltIns
        get() = irContext.irBuiltIns

    val adaptiveNamespaceClass = classSymbol(FqNames.ADAPTIVE_NAMESPACE)

    val armClasses = mutableListOf<ArmClass>()
    val irClasses = mutableMapOf<FqName, IrClass>()
    val armEntryPoints = mutableListOf<ArmEntryPoint>()

    val adaptiveFragmentClass = classSymbol(FqNames.ADAPTIVE_FRAGMENT)
    val adaptiveAdapterClass = classSymbol(FqNames.ADAPTIVE_ADAPTER)
    val adaptiveClosureClass = classSymbol(FqNames.ADAPTIVE_CLOSURE)

    val adaptiveFragmentFactoryClass = classSymbol(FqNames.ADAPTIVE_FRAGMENT_FACTORY)
    val adaptiveFragmentFactoryConstructor = adaptiveFragmentFactoryClass.constructors.single()

    val adaptiveSupportFunctionClass = classSymbol(FqNames.ADAPTIVE_SUPPORT_FUNCTION)
    val adaptiveSupportFunctionInvoke = checkNotNull(adaptiveSupportFunctionClass.getSimpleFunction(Strings.SUPPORT_FUNCTION_INVOKE))
    val adaptiveSupportFunctionIndex = checkNotNull(adaptiveSupportFunctionClass.getPropertyGetter(Strings.SUPPORT_FUNCTION_INDEX))
    val adaptiveSupportFunctionReceivingFragment = checkNotNull(adaptiveSupportFunctionClass.getPropertyGetter(Strings.SUPPORT_FUNCTION_RECEIVING_FRAGMENT))

    val adaptiveStateValueBindingClass = classSymbol(FqNames.ADAPTIVE_STATE_VALUE_BINDING)
    val adaptivePropertyMetadataClass = classSymbol(FqNames.ADAPTIVE_PROPERTY_METADATA)

    val index = property(Strings.INDEX)
    val parent = property(Strings.PARENT)

    val create = function(Strings.CREATE)
    val mount = function(Strings.MOUNT)

    val haveToPatch = function(Strings.HAVE_TO_PATCH)
    val getCreateClosureDirtyMask = function(Strings.GET_CREATE_CLOSURE_DIRTY_MASK)
    val getThisClosureDirtyMask = function(Strings.GET_THIS_CLOSURE_DIRTY_MASK)
    val getCreateClosureVariable = function(Strings.GET_CREATE_CLOSURE_VARIABLE)
    val getThisClosureVariable = function(Strings.GET_THIS_CLOSURE_VARIABLE)
    val setStateVariable = function(Strings.SET_STATE_VARIABLE)

    val arrayGet = checkNotNull(irContext.irBuiltIns.arrayClass.getSimpleFunction("get"))

    val helperFunctions = listOf(
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_ADAPTER).single(),
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_FRAGMENT).single(),
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_THIS_STATE).single()
    )

    val adaptiveStateApiClass = classSymbol(FqNames.ADAPTIVE_STATE_API)

    private fun property(name: String) =
        adaptiveFragmentClass.owner.properties.filter { it.name.asString() == name }.map { it.symbol }.toList()

    private fun function(name: String) =
        adaptiveFragmentClass.functions.single { it.owner.name.asString() == name }

}

