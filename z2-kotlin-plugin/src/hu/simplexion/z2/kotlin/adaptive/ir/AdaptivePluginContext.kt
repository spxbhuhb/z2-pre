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
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.FqName

class AdaptivePluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    override val runtimePackage = Strings.ADAPTIVE_RUNTIME_PACKAGE

    val withTrace = options.adaptiveTrace

    var compilationError = false

    val messages = mutableListOf<PluginMessage>()

    val armClasses = mutableListOf<ArmClass>()
    val armEntryPoints = mutableListOf<ArmEntryPoint>()

    val airClasses = mutableMapOf<FqName, AirClass>()
    val airEntryPoints = mutableListOf<AirEntryPoint>()

    val adaptiveFragmentClass = classSymbol(FqNames.ADAPTIVE_FRAGMENT_CLASS)
    val adaptiveFragmentType = adaptiveFragmentClass.defaultType

    val adaptiveGeneratedFragmentClass = classSymbol(FqNames.ADAPTIVE_GENERATED_FRAGMENT_CLASS)

    val adaptiveAdapterClass = classSymbol(FqNames.ADAPTIVE_ADAPTER_CLASS)
    val adaptiveAdapterType = adaptiveAdapterClass.defaultType
    val adaptiveAdapterTrace = adaptiveAdapterClass.functionByName(Strings.ADAPTIVE_ADAPTER_TRACE_FUN)

    val adaptiveClosureClass = classSymbol(FqNames.ADAPTIVE_CLOSURE_CLASS)

    val adaptiveBridgeClass = classSymbol(FqNames.ADAPTIVE_BRIDGE_CLASS)
    val adaptiveBridgeType = adaptiveBridgeClass.defaultType

    val adaptiveAdapter = property(Strings.ADAPTIVE_ADAPTER_PROP)
    val adaptiveClosure = property(Strings.ADAPTIVE_CLOSURE_PROP)
    val adaptiveParent = property(Strings.ADAPTIVE_PARENT_PROP)
    val adaptiveExternalPatch = property(Strings.ADAPTIVE_EXTERNAL_PATCH_PROP)
    val adaptiveFragment = property(Strings.ADAPTIVE_CONTAINED_FRAGMENT_PROP)

    val adaptiveCreate = function(Strings.ADAPTIVE_CREATE_FUN)
    val adaptiveMount = function(Strings.ADAPTIVE_MOUNT_FUN)
    val adaptivePatch = function(Strings.ADAPTIVE_PATCH_FUN)
    val adaptiveDispose = function(Strings.ADAPTIVE_DISPOSE_FUN)
    val adaptiveUnmount = function(Strings.ADAPTIVE_UNMOUNT_FUN)

    val adaptiveSymbolMap = AdaptiveSymbolMap(this)

    val implicit0SymbolMap = adaptiveSymbolMap.getSymbolMap(FqNames.ADAPTIVE_ANONYMOUS_CLASS)

    private fun property(name: String) =
        adaptiveGeneratedFragmentClass.owner.properties.filter { it.name.asString() == name }.map { it.symbol }.toList()

    private fun function(name: String) =
        listOf(adaptiveGeneratedFragmentClass.functions.single { it.owner.name.asString() == name })

    class PluginMessage(
        val severity: IrMessageLogger.Severity,
        val message : String,
        val location: IrMessageLogger.Location
    ) {
        override fun toString(): String {
            return("[$severity]  $location  $message")
        }
    }

    fun report(severity: IrMessageLogger.Severity, message: String, location: IrMessageLogger.Location) {
        messages += PluginMessage(severity, message, location).also { println(it) }
    }
}

