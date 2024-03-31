/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirEntryPoint
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmEntryPoint
import hu.simplexion.z2.kotlin.util.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.FqName

class AdaptivePluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    override val runtimePackage = Strings.RUNTIME_PACKAGE

    val withTrace = options.adaptiveTrace

    var compilationError = false

    val messages = mutableListOf<PluginMessage>()

    val adaptiveNamespaceClass = classSymbol(FqNames.ADAPTIVE_NAMESPACE)

    val armClasses = mutableListOf<ArmClass>()
    val armEntryPoints = mutableListOf<ArmEntryPoint>()

    val airClasses = mutableMapOf<FqName, AirClass>()
    val airEntryPoints = mutableListOf<AirEntryPoint>()

    val adaptiveFragmentClass = classSymbol(FqNames.ADAPTIVE_FRAGMENT)
    val adaptiveFragmentType = adaptiveFragmentClass.defaultType

    val adaptiveGeneratedFragmentClass = classSymbol(FqNames.ADAPTIVE_GENERATED_FRAGMENT)

    val adaptiveAdapterClass = classSymbol(FqNames.ADAPTIVE_ADAPTER)
    val adaptiveAdapterType = adaptiveAdapterClass.defaultType
    val adaptiveAdapterTrace = adaptiveAdapterClass.functionByName(Strings.TRACE)

    val adaptiveClosureClass = classSymbol(FqNames.ADAPTIVE_CLOSURE)

    val adaptiveBridgeClass = classSymbol(FqNames.ADAPTIVE_BRIDGE)
    val adaptiveBridgeType = adaptiveBridgeClass.defaultType

    val adaptiveFragmentFactoryClass = classSymbol(FqNames.ADAPTIVE_FRAGMENT_FACTORY)
    val adaptiveFragmentFactoryConstructor = adaptiveFragmentFactoryClass.constructors.single()

    val adaptiveSupportFunctionClass = classSymbol(FqNames.ADAPTIVE_SUPPORT_FUNCTION)

    val adapter = property(Strings.ADAPTER)
    val index = property(Strings.INDEX)
    val parent = property(Strings.PARENT)

    val dirtyMask = property(Strings.DIRTY_MASK)

    val build = function(Strings.BUILD)
    val patchDescendant = function(Strings.PATCH_DESCENDANT)
    val invoke = function(Strings.INVOKE)

    val create = function(Strings.CREATE)
    val mount = function(Strings.MOUNT)
    val patchInternal = function(Strings.PATCH_INTERNAL)

    val haveToPatch = function(Strings.HAVE_TO_PATCH)
    val getClosureMask = function(Strings.GET_CLOSURE_DIRTY_MASK)
    val getClosureVariable = function(Strings.GET_CLOSURE_VARIABLE)
    val setStateVariable = function(Strings.SET_STATE_VARIABLE)

    private fun property(name: String) =
        adaptiveGeneratedFragmentClass.owner.properties.filter { it.name.asString() == name }.map { it.symbol }.toList()

    private fun function(name: String) =
        adaptiveGeneratedFragmentClass.functions.single { it.owner.name.asString() == name }

    class PluginMessage(
        val severity: IrMessageLogger.Severity,
        val message: String,
        val location: IrMessageLogger.Location
    ) {
        override fun toString(): String {
            return ("[$severity]  $location  $message")
        }
    }

    fun report(severity: IrMessageLogger.Severity, message: String, location: IrMessageLogger.Location) {
        messages += PluginMessage(severity, message, location).also { println(it) }
    }
}

