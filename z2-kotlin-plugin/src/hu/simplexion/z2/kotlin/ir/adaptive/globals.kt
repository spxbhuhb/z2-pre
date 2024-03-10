/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive

import hu.simplexion.z2.kotlin.ir.adaptive.plugin.AdaptiveRootNameStrategy
import hu.simplexion.z2.kotlin.ir.adaptive.util.capitalizeFirstChar
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

const val ADAPTIVE_ANNOTATION = "hu.simplexion.z2.adaptive.Adaptive"

val ADAPTIVE_FRAGMENT_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveFragment")
val ADAPTIVE_GENERATED_FRAGMENT_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveGeneratedFragment")
val ADAPTIVE_ADAPTER_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveAdapter")
val ADAPTIVE_BRIDGE_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveBridge")
val ADAPTIVE_BLOCK_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveSequence")
val ADAPTIVE_WHEN_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveWhen")
val ADAPTIVE_FOR_LOOP_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveForLoop")
val ADAPTIVE_ENTRY_FUNCTION = listOf("hu", "simplexion", "z2", "adaptive", "adaptive")
val ADAPTIVE_ANONYMOUS_CLASS = listOf("hu", "simplexion", "z2", "adaptive", "AdaptiveAnonymous")

object Strings {
    const val ADAPTIVE_BUILDER_PARENT = "parent"
    const val ADAPTIVE_EXTERNAL_PATCH_FRAGMENT = "fragment"
    const val ADAPTIVE_CLOSURE_CLASS = "hu.simplexion.z2.adaptive.AdaptiveClosure"

}

object Names {
    val ADAPTIVE_EXTERNAL_PATCH_FRAGMENT = Name.identifier(Strings.ADAPTIVE_EXTERNAL_PATCH_FRAGMENT)
}

object FqNames {
    val ADAPTIVE_CLOSURE_CLASS = FqName.fromSegments(Strings.ADAPTIVE_CLOSURE_CLASS.split('.'))
}

object Indices {

    /**
     * Fragment constructor arguments.
     *
     * 1. adaptiveAdapter
     * 2. adaptiveClosure
     * 3. adaptiveParent
     * 4. adaptivePatchExternal
     */
    const val ADAPTIVE_FRAGMENT_ARGUMENT_COUNT = 4

    const val ADAPTIVE_FRAGMENT_ADAPTER = 0
    const val ADAPTIVE_FRAGMENT_CLOSURE = 1
    const val ADAPTIVE_FRAGMENT_PARENT = 2
    const val ADAPTIVE_FRAGMENT_EXTERNAL_PATCH = 3

    /**
     * Builder function arguments.
     */
    const val ADAPTIVE_BUILDER_PARENT = 0

    /**
     * External patch function arguments
     */
    const val ADAPTIVE_EXTERNAL_PATCH_FRAGMENT = 0

}

const val ADAPTIVE_BLOCK_ARGUMENT_COUNT = 2
const val ADAPTIVE_BLOCK_ARGUMENT_INDEX_FRAGMENTS = 1

const val ADAPTIVE_WHEN_ARGUMENT_COUNT = 3
const val ADAPTIVE_WHEN_ARGUMENT_INDEX_SELECT = 1
const val ADAPTIVE_WHEN_ARGUMENT_INDEX_FRAGMENTS = 2

const val ADAPTIVE_TRACE_ARGUMENT_COUNT = 3
const val ADAPTIVE_TRACE_ARGUMENT_NAME = 0
const val ADAPTIVE_TRACE_ARGUMENT_POINT = 1
const val ADAPTIVE_TRACE_ARGUMENT_DATA = 2

const val ADAPTIVE_STATE_VARIABLE_LIMIT = 60

/**
 * Bridge type parameter for classes.
 */
const val ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE = 0

const val ADAPTIVE_ROOT_CLASS_PREFIX = "AdaptiveRoot"
const val ADAPTIVE_ANONYMOUS_PREFIX = "AdaptiveAnonymous"

const val ADAPTIVE_BT = "BT" // type parameter for fragment, Bridge Type
const val ADAPTIVE_ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter

const val ADAPTIVE_ADAPTER_TRACE = "trace" // name of the trace function in the adapter class

const val ADAPTIVE_CREATE = "adaptiveCreate"
const val ADAPTIVE_MOUNT = "adaptiveMount"
const val ADAPTIVE_PATCH = "adaptivePatch"
const val ADAPTIVE_UNMOUNT = "adaptiveUnmount"
const val ADAPTIVE_DISPOSE = "adaptiveDispose"

const val ADAPTIVE_INVALIDATE = "adaptiveInvalidate"
const val ADAPTIVE_DIRTY = "adaptiveDirty"

const val ADAPTIVE_ADAPTER = "adaptiveAdapter"
const val ADAPTIVE_CLOSURE = "adaptiveClosure"
const val ADAPTIVE_PARENT = "adaptiveParent"
const val ADAPTIVE_EXTERNAL_PATCH = "adaptiveExternalPatch"
const val ADAPTIVE_FRAGMENT = "containedFragment"
const val ADAPTIVE_MASK = "mask"

const val ADAPTIVE_BLOCK = "adaptiveBlock"
const val ADAPTIVE_BRANCH = "adaptiveBranch"
const val ADAPTIVE_CALL = "rumCall"
const val ADAPTIVE_HIGHER_ORDER_CALL = "adaptiveHigherOrderCall"
const val ADAPTIVE_FOR_LOOP = "adaptiveForLoop"
const val ADAPTIVE_WHEN = "adaptiveWhen"

const val ADAPTIVE_SELECT = "adaptiveSelect"
const val ADAPTIVE_EXTERNAL_PATCH_OF_CHILD = "adaptiveExternalPatch"
const val ADAPTIVE_BUILDER = "adaptiveBuilder"

val ADAPTIVE_FQN_FRAGMENT_CLASS = FqName.fromSegments(ADAPTIVE_FRAGMENT_CLASS)
val ADAPTIVE_FQN_GENERATED_FRAGMENT_CLASS = FqName.fromSegments(ADAPTIVE_GENERATED_FRAGMENT_CLASS)
val ADAPTIVE_FQN_ADAPTER_CLASS = FqName.fromSegments(ADAPTIVE_ADAPTER_CLASS)
val ADAPTIVE_FQN_BRIDGE_CLASS = FqName.fromSegments(ADAPTIVE_BRIDGE_CLASS)
val ADAPTIVE_FQN_BLOCK_CLASS = FqName.fromSegments(ADAPTIVE_BLOCK_CLASS)
val ADAPTIVE_FQN_WHEN_CLASS = FqName.fromSegments(ADAPTIVE_WHEN_CLASS)
val ADAPTIVE_FQN_FOR_LOOP_CLASS = FqName.fromSegments(ADAPTIVE_FOR_LOOP_CLASS)
val ADAPTIVE_FQN_ENTRY_FUNCTION = FqName.fromSegments(ADAPTIVE_ENTRY_FUNCTION)
val ADAPTIVE_FQN_ANONYMOUS_CLASS = FqName.fromSegments(ADAPTIVE_ANONYMOUS_CLASS)

fun IrFunction.toAdaptiveClassFqName(adaptiveContext: AdaptivePluginContext, anonymous: Boolean): FqName {
    val parent = kotlinFqName.parentOrNull() ?: FqName.ROOT
    return when {
        anonymous -> {
            parent.child(Name.identifier("$ADAPTIVE_ANONYMOUS_PREFIX${startOffset}"))
        }
        isAnonymousFunction || name.asString() == "<anonymous>" -> {
            val postfix = when (adaptiveContext.rootNameStrategy) {
                AdaptiveRootNameStrategy.StartOffset -> this.file.name.replace(".kt", "").capitalizeFirstChar() + startOffset.toString()
                AdaptiveRootNameStrategy.NoPostfix -> ""
            }
            parent.child(Name.identifier("$ADAPTIVE_ROOT_CLASS_PREFIX$postfix"))
        }

        else -> parent.child(Name.identifier("Adaptive" + name.identifier.capitalizeFirstChar()))
    }
}