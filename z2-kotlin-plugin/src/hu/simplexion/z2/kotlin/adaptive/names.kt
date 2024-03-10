/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object Strings {
    const val ADAPTIVE_RUNTIME_PACKAGE = "hu.simplexion.z2.adaptive.Adaptive"

    const val ADAPTIVE_ANNOTATION = "hu.simplexion.z2.adaptive.Adaptive"

    const val ADAPTIVE_ROOT_CLASS_PREFIX = "AdaptiveRoot"
    const val ADAPTIVE_ANONYMOUS_PREFIX = "AdaptiveAnonymous"

    const val ADAPTIVE_SELECT_FUN = "adaptiveSelect"

    const val ADAPTIVE_BUILDER_FUN = "adaptiveBuilder"
    const val ADAPTIVE_BUILDER_PARENT_ARG = "parent"

    const val ADAPTIVE_EXTERNAL_PATCH_FUN = "adaptiveExternalPatch"
    const val ADAPTIVE_EXTERNAL_PATCH_FRAGMENT_ARG = "fragment"

    const val ADAPTIVE_CLOSURE_CLASS = "hu.simplexion.z2.adaptive.AdaptiveClosure"
    const val ADAPTIVE_FRAGMENT_CLASS = "hu.simplexion.z2.adaptive.AdaptiveFragment"
    const val ADAPTIVE_GENERATED_FRAGMENT_CLASS = "hu.simplexion.z2.adaptive.AdaptiveGeneratedFragment"
    const val ADAPTIVE_ADAPTER_CLASS = "hu.simplexion.z2.adaptive.AdaptiveAdapter"
    const val ADAPTIVE_BRIDGE_CLASS = "hu.simplexion.z2.adaptive.AdaptiveBridge"
    const val ADAPTIVE_BLOCK_CLASS = "hu.simplexion.z2.adaptive.AdaptiveSequence"
    const val ADAPTIVE_WHEN_CLASS = "hu.simplexion.z2.adaptive.AdaptiveWhen"
    const val ADAPTIVE_FOR_LOOP_CLASS = "hu.simplexion.z2.adaptive.AdaptiveForLoop"
    const val ADAPTIVE_ENTRY_FUNCTION = "hu.simplexion.z2.adaptive.adaptive"
    const val ADAPTIVE_ANONYMOUS_CLASS = "hu.simplexion.z2.adaptive.AdaptiveAnonymous"

    const val ADAPTIVE_CREATE_FUN = "adaptiveCreate"
    const val ADAPTIVE_MOUNT_FUN = "adaptiveMount"
    const val ADAPTIVE_PATCH_FUN = "adaptivePatch"
    const val ADAPTIVE_UNMOUNT_FUN = "adaptiveUnmount"
    const val ADAPTIVE_DISPOSE_FUN = "adaptiveDispose"

    const val ADAPTIVE_INVALIDATE_FUN = "adaptiveInvalidate"
    const val ADAPTIVE_INVALIDATE_MASK_ARG = "mask"

    const val ADAPTIVE_DIRTY_FUN = "adaptiveDirty"

    const val ADAPTIVE_ADAPTER_PROP = "adaptiveAdapter"
    const val ADAPTIVE_CLOSURE_PROP = "adaptiveClosure"
    const val ADAPTIVE_PARENT_PROP = "adaptiveParent"
    const val ADAPTIVE_EXTERNAL_PATCH_PROP = "adaptiveExternalPatch"
    const val ADAPTIVE_CONTAINED_FRAGMENT_PROP = "containedFragment"
    const val ADAPTIVE_MASK = "mask"

    const val ADAPTIVE_ADAPTER_TRACE_FUN = "trace" // name of the trace function in the adapter class

    const val ADAPTIVE_BT = "BT" // type parameter for fragment, Bridge Type
    const val ADAPTIVE_ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter

    fun String.toNameWithPostfix(postfix: Int) =
        Name.identifier("$this$postfix")

    fun String.toNameWithPostfix(postfix: String) =
        Name.identifier("$this$postfix")
}

object Names {
    val ADAPTIVE_EXTERNAL_PATCH_FRAGMENT = Name.identifier(Strings.ADAPTIVE_EXTERNAL_PATCH_FRAGMENT_ARG)

    val ADAPTIVE_ADAPTER_PROP = Name.identifier(Strings.ADAPTIVE_ADAPTER_PROP)
    val ADAPTIVE_CLOSURE_PROP = Name.identifier(Strings.ADAPTIVE_CLOSURE_PROP)
    val ADAPTIVE_PARENT_PROP = Name.identifier(Strings.ADAPTIVE_PARENT_PROP)
    val ADAPTIVE_EXTERNAL_PATCH_PROP = Name.identifier(Strings.ADAPTIVE_EXTERNAL_PATCH_PROP)
    val ADAPTIVE_CONTAINED_FRAGMENT_PROP = Name.identifier(Strings.ADAPTIVE_CONTAINED_FRAGMENT_PROP)

    val ADAPTIVE_BT = Name.identifier(Strings.ADAPTIVE_BT)

    val ADAPTIVE_CREATE_FUN = Name.identifier(Strings.ADAPTIVE_CREATE_FUN)
    val ADAPTIVE_MOUNT_FUN = Name.identifier(Strings.ADAPTIVE_MOUNT_FUN)
    val ADAPTIVE_PATCH_FUN = Name.identifier(Strings.ADAPTIVE_PATCH_FUN)
    val ADAPTIVE_UNMOUNT_FUN = Name.identifier(Strings.ADAPTIVE_UNMOUNT_FUN)
    val ADAPTIVE_DISPOSE_FUN = Name.identifier(Strings.ADAPTIVE_DISPOSE_FUN)

    val ADAPTIVE_INVALIDATE_MASK_ARG = Name.identifier(Strings.ADAPTIVE_INVALIDATE_MASK_ARG)
}

object FqNames {
    val ADAPTIVE_CLOSURE_CLASS = FqName.fromSegments(Strings.ADAPTIVE_CLOSURE_CLASS.split('.'))
    val ADAPTIVE_FRAGMENT_CLASS = FqName.fromSegments(Strings.ADAPTIVE_FRAGMENT_CLASS.split('.'))
    val ADAPTIVE_GENERATED_FRAGMENT_CLASS = FqName.fromSegments(Strings.ADAPTIVE_GENERATED_FRAGMENT_CLASS.split('.'))
    val ADAPTIVE_ADAPTER_CLASS = FqName.fromSegments(Strings.ADAPTIVE_ADAPTER_CLASS.split('.'))
    val ADAPTIVE_BRIDGE_CLASS = FqName.fromSegments(Strings.ADAPTIVE_BRIDGE_CLASS.split('.'))
    val ADAPTIVE_BLOCK_CLASS = FqName.fromSegments(Strings.ADAPTIVE_BLOCK_CLASS.split('.'))
    val ADAPTIVE_WHEN_CLASS = FqName.fromSegments(Strings.ADAPTIVE_WHEN_CLASS.split('.'))
    val ADAPTIVE_FOR_LOOP_CLASS = FqName.fromSegments(Strings.ADAPTIVE_FOR_LOOP_CLASS.split('.'))
    val ADAPTIVE_ENTRY_FUNCTION = FqName.fromSegments(Strings.ADAPTIVE_ENTRY_FUNCTION.split('.'))
    val ADAPTIVE_ANONYMOUS_CLASS = FqName.fromSegments(Strings.ADAPTIVE_ANONYMOUS_CLASS.split('.'))
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

    /**
     * Adapter trace function arguments
     */
    const val ADAPTIVE_TRACE_ARGUMENT_COUNT = 3
    const val ADAPTIVE_TRACE_ARGUMENT_NAME = 0
    const val ADAPTIVE_TRACE_ARGUMENT_POINT = 1
    const val ADAPTIVE_TRACE_ARGUMENT_DATA = 2

    /**
     * Bridge type parameter for classes.
     */
    const val ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE = 0


    const val ADAPTIVE_BLOCK_ARGUMENT_COUNT = 2
    const val ADAPTIVE_BLOCK_ARGUMENT_INDEX_FRAGMENTS = 1

}

const val ADAPTIVE_WHEN_ARGUMENT_COUNT = 3
const val ADAPTIVE_WHEN_ARGUMENT_INDEX_SELECT = 1
const val ADAPTIVE_WHEN_ARGUMENT_INDEX_FRAGMENTS = 2

const val ADAPTIVE_BLOCK = "adaptiveBlock"
const val ADAPTIVE_BRANCH = "adaptiveBranch"
const val ADAPTIVE_CALL = "armCall"
const val ADAPTIVE_HIGHER_ORDER_CALL = "adaptiveHigherOrderCall"
const val ADAPTIVE_FOR_LOOP = "adaptiveForLoop"
const val ADAPTIVE_WHEN = "adaptiveWhen"

