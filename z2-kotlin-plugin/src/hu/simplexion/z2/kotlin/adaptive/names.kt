/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object Strings {
    const val RUNTIME_PACKAGE = "hu.simplexion.z2.adaptive"

    const val ADAPTIVE_NAMESPACE = "Adaptive"
    const val ENTRY_FUNCTION = "adaptive"

    const val ADAPTIVE_ROOT = "AdaptiveRoot"
    const val ADAPTIVE_ANONYMOUS = "AdaptiveAnonymous"
    const val ADAPTIVE_FRAGMENT = "AdaptiveFragment"
    const val ADAPTIVE_CLOSURE = "AdaptiveClosure"
    const val ADAPTIVE_ADAPTER = "AdaptiveAdapter"
    const val ADAPTIVE_BRIDGE = "AdaptiveBridge"
    const val ADAPTIVE_SEQUENCE = "AdaptiveSequence"
    const val ADAPTIVE_SELECT = "AdaptiveSelect"
    const val ADAPTIVE_LOOP = "AdaptiveLoop"
    const val ADAPTIVE_FRAGMENT_FACTORY = "AdaptiveFragmentFactory"
    const val ADAPTIVE_SUPPORT_FUNCTION = "AdaptiveSupportFunction"

    const val GEN_BUILD = "genBuild"
    const val GEN_PATCH_DESCENDANT = "genPatchDescendant"
    const val GEN_INVOKE = "genInvoke"

    const val CREATE = "create"
    const val MOUNT = "mount"
    const val GEN_PATCH_INTERNAL = "genPatchInternal"

    const val HAVE_TO_PATCH = "haveToPatch"
    const val GET_CREATE_CLOSURE_DIRTY_MASK = "getCreateClosureDirtyMask"
    const val SET_STATE_VARIABLE = "setStateVariable"
    const val GET_CREATE_CLOSURE_VARIABLE = "getCreateClosureVariable"
    const val GET_THIS_CLOSURE_VARIABLE = "getThisClosureVariable"
    const val INVALID_INDEX = "invalidIndex"

    const val PARENT = "parent"
    const val INDEX = "index"
    const val DECLARATION_INDEX = "declarationIndex"
    const val FRAGMENT = "fragment"
    const val SUPPORT_FUNCTION = "supportFunction"
    const val SUPPORT_FUNCTION_INDEX = "supportFunctionIndex"
    const val SUPPORT_FUNCTION_CLOSURE = "supportFunctionClosure"
    const val ARGUMENTS = "arguments"

    const val ADAPTER = "adapter"
    const val DIRTY_MASK = "dirtyMask"

    const val SUPPORT_FUNCTION_INVOKE = "invoke"
    const val TRACE = "trace" // name of the trace function in the adapter class

    const val BT = "BT" // type parameter for fragment, Bridge Type
    const val ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter
}

object Names {
    val PARENT = Name.identifier(Strings.PARENT)
    val INDEX = Name.identifier(Strings.INDEX)

    val ADAPTER = Name.identifier(Strings.ADAPTER)
    val DIRTY_MASK = Name.identifier(Strings.DIRTY_MASK)

    val BT = Name.identifier(Strings.BT)

    val GEN_BUILD = Name.identifier(Strings.GEN_BUILD)
    val GEN_PATCH_DESCENDANT = Name.identifier(Strings.GEN_PATCH_DESCENDANT)
    val GEN_INVOKE = Name.identifier(Strings.GEN_INVOKE)

    val GEN_PATCH_INTERNAL = Name.identifier(Strings.GEN_PATCH_INTERNAL)

    val FRAGMENT = Name.identifier(Strings.FRAGMENT)
    val DECLARATION_INDEX = Name.identifier(Strings.DECLARATION_INDEX)
    val SUPPORT_FUNCTION = Name.identifier(Strings.SUPPORT_FUNCTION)
    val ARGUMENTS = Name.identifier(Strings.ARGUMENTS)

    val TRACE = Name.identifier(Strings.TRACE)
}

object FqNames {
    val String.runtime
        get() = FqName(Strings.RUNTIME_PACKAGE + "." + this)

    val ADAPTIVE_NAMESPACE = Strings.ADAPTIVE_NAMESPACE.runtime
    val ADAPTIVE_CLOSURE = Strings.ADAPTIVE_CLOSURE.runtime
    val ADAPTIVE_FRAGMENT = Strings.ADAPTIVE_FRAGMENT.runtime
    val ADAPTIVE_ADAPTER = Strings.ADAPTIVE_ADAPTER.runtime
    val ADAPTIVE_BRIDGE = Strings.ADAPTIVE_BRIDGE.runtime
    val ADAPTIVE_SEQUENCE = Strings.ADAPTIVE_SEQUENCE.runtime
    val ADAPTIVE_SELECT = Strings.ADAPTIVE_SELECT.runtime
    val ADAPTIVE_LOOP = Strings.ADAPTIVE_LOOP.runtime
    val ADAPTIVE_ENTRY_FUNCTION = Strings.ENTRY_FUNCTION.runtime
    val ADAPTIVE_ANONYMOUS = Strings.ADAPTIVE_ANONYMOUS.runtime
    val ADAPTIVE_FRAGMENT_FACTORY = Strings.ADAPTIVE_FRAGMENT_FACTORY.runtime
    val ADAPTIVE_SUPPORT_FUNCTION = Strings.ADAPTIVE_SUPPORT_FUNCTION.runtime
}

object Indices {

    /**
     * Fragment constructor arguments.
     *
     * 1. adapter
     * 2. parent
     * 3. index
     */
    const val ADAPTIVE_FRAGMENT_ARGUMENT_COUNT = 3

    const val ADAPTIVE_FRAGMENT_ADAPTER = 0
    const val ADAPTIVE_FRAGMENT_PARENT = 1
    const val ADAPTIVE_FRAGMENT_INDEX = 2

    /**
     * AdaptiveGeneratedFragment constructor arguments
     */
    const val ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT = 4
    const val ADAPTIVE_GENERATED_FRAGMENT_ADAPTER = 0
    const val ADAPTIVE_GENERATED_FRAGMENT_PARENT = 1
    const val ADAPTIVE_GENERATED_FRAGMENT_INDEX = 2
    const val ADAPTIVE_GENERATED_FRAGMENT_STATE_SIZE = 3

    /**
     * `build(parent, declarationIndex)` arguments
     */
    const val BUILD_PARENT = 0
    const val BUILD_DECLARATION_INDEX = 1

    /**
     * `patchExternal(fragment)` arguments
     */
    const val PATCH_EXTERNAL_FRAGMENT = 0

    /**
     * `invoke(supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>): Any?` arguments
     */
    const val INVOKE_SUPPORT_FUNCTION = 0
    const val INVOKE_ARGUMENTS = 1

    /**
     * `setStateVariable(index, value)` arguments
     */
    const val SET_STATE_VARIABLE_ARGUMENT_COUNT = 2

    const val SET_STATE_VARIABLE_INDEX = 0
    const val SET_STATE_VARIABLE_VALUE = 1

    /**
     * `getCreateClosureVariable(index)` arguments
     * `getThisClosureVariable(index)` arguments
     */
    const val GET_CLOSURE_VARIABLE_ARGUMENT_COUNT = 1

    const val GET_CLOSURE_VARIABLE_INDEX = 0

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

    /**
     * Fragment factory arguments
     */
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT = 2

    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT = 0
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX = 1

    /**
     * AdaptiveSupportFunction constructor arguments
     */
    const val ADAPTIVE_SUPPORT_FUNCTION_ARGUMENT_COUNT = 2

    const val ADAPTIVE_SUPPORT_FUNCTION_FRAGMENT = 0
    const val ADAPTIVE_SUPPORT_FUNCTION_INDEX = 1

    /**
     * Structural fragment state indices
     */
    const val ADAPTIVE_SEQUENCE_ITEM_INDICES = 0
    const val ADAPTIVE_SELECT_BRANCH = 0
    const val ADAPTIVE_LOOP_ITERATOR = 0
    const val ADAPTIVE_LOOP_FACTORY = 1

    /**
     * AdaptiveFragment.invalidIndex(index) arguments
     */
    const val INVALID_INDEX_INDEX = 0
}