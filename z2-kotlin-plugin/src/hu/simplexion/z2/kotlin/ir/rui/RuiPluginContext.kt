/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui

import hu.simplexion.z2.kotlin.ir.Z2PluginContext
import hu.simplexion.z2.kotlin.ir.rui.air.AirClass
import hu.simplexion.z2.kotlin.ir.rui.air.AirEntryPoint
import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiOptions
import hu.simplexion.z2.kotlin.ir.rui.rum.RumClass
import hu.simplexion.z2.kotlin.ir.rui.rum.RumEntryPoint
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

class RuiPluginContext(
    override val irContext: IrPluginContext,
    override val moduleFragment: IrModuleFragment,
    options: RuiOptions,
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
    override val pluginLogFile: Path? = pluginLogDir?.resolve("rui-log-$pluginLogTimestamp.txt").also { it?.createFile() }

    var compilationError = false

    val rumClasses = mutableListOf<RumClass>()
    val rumEntryPoints = mutableListOf<RumEntryPoint>()

    val airClasses = mutableMapOf<FqName, AirClass>()
    val airEntryPoints = mutableListOf<AirEntryPoint>()

    val ruiFragmentClass = classSymbol(RUI_FQN_FRAGMENT_CLASS)
    val ruiFragmentType = ruiFragmentClass.defaultType

    val ruiGeneratedFragmentClass = classSymbol(RUI_FQN_GENERATED_FRAGMENT_CLASS)

    val ruiAdapterClass = classSymbol(RUI_FQN_ADAPTER_CLASS)
    val ruiAdapterType = ruiAdapterClass.defaultType
    val ruiAdapterTrace = ruiAdapterClass.functionByName(RUI_ADAPTER_TRACE)

    val ruiClosureClass = classSymbol(FqNames.RUI_CLOSURE_CLASS)

    val ruiBridgeClass = classSymbol(RUI_FQN_BRIDGE_CLASS)
    val ruiBridgeType = ruiBridgeClass.defaultType

    val ruiAdapter = property(RUI_ADAPTER)
    val ruiClosure = property(RUI_CLOSURE)
    val ruiParent = property(RUI_PARENT)
    val ruiExternalPatch = property(RUI_EXTERNAL_PATCH)
    val ruiFragment = property(RUI_FRAGMENT)

    val ruiCreate = function(RUI_CREATE)
    val ruiMount = function(RUI_MOUNT)
    val ruiPatch = function(RUI_PATCH)
    val ruiDispose = function(RUI_DISPOSE)
    val ruiUnmount = function(RUI_UNMOUNT)

    val ruiSymbolMap = RuiSymbolMap(this)

    val implicit0SymbolMap = ruiSymbolMap.getSymbolMap(RUI_FQN_ANONYMOUS_CLASS)

    private fun property(name: String) =
        ruiGeneratedFragmentClass.owner.properties.filter { it.name.asString() == name }.map { it.symbol }.toList()

    private fun function(name: String) =
        listOf(ruiGeneratedFragmentClass.functions.single { it.owner.name.asString() == name })

}

