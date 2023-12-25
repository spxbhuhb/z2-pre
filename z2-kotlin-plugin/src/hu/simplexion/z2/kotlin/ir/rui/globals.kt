/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui

import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiRootNameStrategy
import hu.simplexion.z2.kotlin.ir.rui.util.capitalizeFirstChar
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.parentOrNull

const val OPTION_NAME_ANNOTATION = "annotation"
const val OPTION_NAME_DUMP_POINT = "dump-point"
const val OPTION_NAME_ROOT_NAME_STRATEGY = "root-name-strategy"
const val OPTION_NAME_TRACE = "trace"
const val OPTION_NAME_EXPORT_STATE = "export-state"
const val OPTION_NAME_IMPORT_STATE = "import-state"
const val OPTION_NAME_PRINT_DUMPS = "print-dumps"
const val OPTION_NAME_PLUGIN_LOG_DIR = "plugin-log-dir"

const val RUI_ANNOTATION = "hu.simplexion.z2.rui.Rui"

val RUI_FRAGMENT_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiFragment")
val RUI_GENERATED_FRAGMENT_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiGeneratedFragment")
val RUI_ADAPTER_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiAdapter")
val RUI_BRIDGE_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiBridge")
val RUI_BLOCK_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiBlock")
val RUI_WHEN_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiWhen")
val RUI_FOR_LOOP_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiForLoop")
val RUI_ENTRY_FUNCTION = listOf("hu", "simplexion", "z2", "rui", "rui")
val RUI_IMPLICIT0_CLASS = listOf("hu", "simplexion", "z2", "rui", "RuiImplicit0")

/**
 * Number of Rui related constructor arguments.
 *
 * 1. ruiAdapter
 * 2. ruiScope
 * 3. ruiPatchExternal
 */
const val RUI_FRAGMENT_ARGUMENT_COUNT = 3
const val RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER = 0
const val RUI_FRAGMENT_ARGUMENT_INDEX_SCOPE = 1
const val RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH = 2

const val RUI_BUILDER_ARGUMENT_COUNT = 1
const val RUI_BUILDER_ARGUMENT_INDEX_START_SCOPE = 0

const val RUI_BLOCK_ARGUMENT_COUNT = 2
const val RUI_BLOCK_ARGUMENT_INDEX_FRAGMENTS = 1

const val RUI_WHEN_ARGUMENT_COUNT = 3
const val RUI_WHEN_ARGUMENT_INDEX_SELECT = 1
const val RUI_WHEN_ARGUMENT_INDEX_FRAGMENTS = 2

const val RUI_TRACE_ARGUMENT_COUNT = 3
const val RUI_TRACE_ARGUMENT_NAME = 0
const val RUI_TRACE_ARGUMENT_POINT = 1
const val RUI_TRACE_ARGUMENT_DATA = 2

const val RUI_PATCH_ARGUMENT_INDEX_SCOPE_MASK = 0

const val RUI_EXTERNAL_PATCH_ARGUMENT_INDEX_FRAGMENT = 0
const val RUI_EXTERNAL_PATCH_ARGUMENT_INDEX_SCOPE_MASK = 1

val RUI_EXTERNAL_PATCH_ARGUMENT_NAME_FRAGMENT = Name.identifier("fragment")
val RUI_EXTERNAL_PATCH_ARGUMENT_NAME_SCOPE_MASK = Name.identifier("scopeMask")

const val RUI_STATE_VARIABLE_LIMIT = 60

/**
 * Bridge type parameter for classes.
 */
const val RUI_FRAGMENT_TYPE_INDEX_BRIDGE = 0

const val RUI_ROOT_CLASS_PREFIX = "RuiRoot"
const val RUI_HIGHER_ORDER_PREFIX = "RuiHigherOrder"

const val RUI_BT = "BT" // type parameter for fragment, Bridge Type
const val RUI_ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter

const val RUI_ADAPTER_TRACE = "trace" // name of the trace function in the adapter class

const val RUI_CREATE = "ruiCreate"
const val RUI_MOUNT = "ruiMount"
const val RUI_PATCH = "ruiPatch"
const val RUI_UNMOUNT = "ruiUnmount"
const val RUI_DISPOSE = "ruiDispose"

const val RUI_INVALIDATE = "ruiInvalidate"
const val RUI_DIRTY = "ruiDirty"

const val RUI_ADAPTER = "ruiAdapter"
const val RUI_SCOPE = "ruiScope"
const val RUI_EXTERNAL_PATCH = "ruiExternalPatch"
const val RUI_FRAGMENT = "containedFragment"
const val RUI_MASK = "mask"

const val RUI_BLOCK = "ruiBlock"
const val RUI_BRANCH = "ruiBranch"
const val RUI_CALL = "rumCall"
const val RUI_HIGHER_ORDER_CALL = "ruiHigherOrderCall"
const val RUI_FOR_LOOP = "ruiForLoop"
const val RUI_WHEN = "ruiWhen"

const val RUI_SELECT = "ruiSelect"
const val RUI_EXTERNAL_PATCH_OF_CHILD = "ruiExternalPatch"
const val RUI_BUILDER = "ruiBuilder"

val RUI_FQN_FRAGMENT_CLASS = FqName.fromSegments(RUI_FRAGMENT_CLASS)
val RUI_FQN_GENERATED_FRAGMENT_CLASS = FqName.fromSegments(RUI_GENERATED_FRAGMENT_CLASS)
val RUI_FQN_ADAPTER_CLASS = FqName.fromSegments(RUI_ADAPTER_CLASS)
val RUI_FQN_BRIDGE_CLASS = FqName.fromSegments(RUI_BRIDGE_CLASS)
val RUI_FQN_BLOCK_CLASS = FqName.fromSegments(RUI_BLOCK_CLASS)
val RUI_FQN_WHEN_CLASS = FqName.fromSegments(RUI_WHEN_CLASS)
val RUI_FQN_FOR_LOOP_CLASS = FqName.fromSegments(RUI_FOR_LOOP_CLASS)
val RUI_FQN_ENTRY_FUNCTION = FqName.fromSegments(RUI_ENTRY_FUNCTION)
val RUI_FQN_IMPLICIT0_CLASS = FqName.fromSegments(RUI_IMPLICIT0_CLASS)

fun IrFunction.toRuiClassFqName(ruiContext: RuiPluginContext, higherOrder: Boolean): FqName {
    val parent = kotlinFqName.parentOrNull() ?: FqName.ROOT
    return when {
        higherOrder -> {
            parent.child(Name.identifier("$RUI_HIGHER_ORDER_PREFIX${startOffset}"))
        }
        isAnonymousFunction || name.asString() == "<anonymous>" -> {
            val postfix = when (ruiContext.rootNameStrategy) {
                RuiRootNameStrategy.StartOffset -> this.file.name.replace(".kt", "").capitalizeFirstChar() + startOffset.toString()
                RuiRootNameStrategy.NoPostfix -> ""
            }
            parent.child(Name.identifier("$RUI_ROOT_CLASS_PREFIX$postfix"))
        }

        else -> parent.child(Name.identifier("Rui" + name.identifier.capitalizeFirstChar()))
    }
}