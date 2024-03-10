/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive

import hu.simplexion.z2.kotlin.ir.Z2PluginContext
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirClass
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirEntryPoint
import hu.simplexion.z2.kotlin.ir.adaptive.plugin.AdaptiveOptions
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumClass
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumEntryPoint
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.FqName
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

class AdaptivePluginContext(
    override val irContext: IrPluginContext,
    override val moduleFragment: IrModuleFragment,
    options: AdaptiveOptions,
    override val messages: MutableList<Z2PluginContext.PluginMessage> = mutableListOf()
) : Z2PluginContext {
    val annotations = options.annotations
    val dumpPoints = options.dumpPoints
    val rootNameStrategy = options.rootNameStrategy
    val withTrace = options.withTrace
    val exportState = options.exportState
    val importState = options.importState
    override val printDumps = options.printDumps

    val pluginLogDir: Path? = options.pluginLogDir?.let { Paths.get(options.pluginLogDir).also { it.createDirectories() } }
    val pluginLogTimestamp: String = DateTimeFormatter.ofPattern("yyyyMMdd'-'HHmmss").format(LocalDateTime.now())
    override val pluginLogFile: Path? = pluginLogDir?.resolve("adaptive-log-$pluginLogTimestamp.txt").also { it?.createFile() }

    var compilationError = false

    val rumClasses = mutableListOf<RumClass>()
    val rumEntryPoints = mutableListOf<RumEntryPoint>()

    val airClasses = mutableMapOf<FqName, AirClass>()
    val airEntryPoints = mutableListOf<AirEntryPoint>()

    val adaptiveFragmentClass = classSymbol(ADAPTIVE_FQN_FRAGMENT_CLASS)
    val adaptiveFragmentType = adaptiveFragmentClass.defaultType

    val adaptiveGeneratedFragmentClass = classSymbol(ADAPTIVE_FQN_GENERATED_FRAGMENT_CLASS)

    val adaptiveAdapterClass = classSymbol(ADAPTIVE_FQN_ADAPTER_CLASS)
    val adaptiveAdapterType = adaptiveAdapterClass.defaultType
    val adaptiveAdapterTrace = adaptiveAdapterClass.functionByName(ADAPTIVE_ADAPTER_TRACE)

    val adaptiveClosureClass = classSymbol(FqNames.ADAPTIVE_CLOSURE_CLASS)

    val adaptiveBridgeClass = classSymbol(ADAPTIVE_FQN_BRIDGE_CLASS)
    val adaptiveBridgeType = adaptiveBridgeClass.defaultType

    val adaptiveAdapter = property(ADAPTIVE_ADAPTER)
    val adaptiveClosure = property(ADAPTIVE_CLOSURE)
    val adaptiveParent = property(ADAPTIVE_PARENT)
    val adaptiveExternalPatch = property(ADAPTIVE_EXTERNAL_PATCH)
    val adaptiveFragment = property(ADAPTIVE_FRAGMENT)

    val adaptiveCreate = function(ADAPTIVE_CREATE)
    val adaptiveMount = function(ADAPTIVE_MOUNT)
    val adaptivePatch = function(ADAPTIVE_PATCH)
    val adaptiveDispose = function(ADAPTIVE_DISPOSE)
    val adaptiveUnmount = function(ADAPTIVE_UNMOUNT)

    val adaptiveSymbolMap = AdaptiveSymbolMap(this)

    val implicit0SymbolMap = adaptiveSymbolMap.getSymbolMap(ADAPTIVE_FQN_ANONYMOUS_CLASS)

    private fun property(name: String) =
        adaptiveGeneratedFragmentClass.owner.properties.filter { it.name.asString() == name }.map { it.symbol }.toList()

    private fun function(name: String) =
        listOf(adaptiveGeneratedFragmentClass.functions.single { it.owner.name.asString() == name })

}

