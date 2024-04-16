/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive

import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object Strings {
    const val RUNTIME_PACKAGE = "hu.simplexion.z2.adaptive"
    const val META_PACKAGE = "hu.simplexion.z2.meta"

    const val ADAPTIVE_NAMESPACE = "Adaptive"
    const val ENTRY_FUNCTION = "adaptive"

    const val ADAPTIVE_ROOT = "AdaptiveRoot"
    const val ADAPTIVE_ANONYMOUS = "AdaptiveAnonymous"
    const val ADAPTIVE_FRAGMENT = "AdaptiveFragment"
    const val ADAPTIVE_CLOSURE = "AdaptiveClosure"
    const val ADAPTIVE_ADAPTER = "AdaptiveAdapter"
    const val ADAPTIVE_SEQUENCE = "AdaptiveSequence"
    const val ADAPTIVE_SELECT = "AdaptiveSelect"
    const val ADAPTIVE_LOOP = "AdaptiveLoop"
    const val ADAPTIVE_FRAGMENT_FACTORY = "AdaptiveFragmentFactory"
    const val ADAPTIVE_SUPPORT_FUNCTION = "AdaptiveSupportFunction"
    const val ADAPTIVE_STATE_VARIABLE_BINDING = "AdaptiveStateVariableBinding"
    const val ADAPTIVE_STATE_API = "AdaptiveStateApi"

    const val PROPERTY_METADATA = "PropertyMetadata"

    const val ROOT_FRAGMENT = "rootFragment"

    const val GEN_BUILD = "genBuild"
    const val GEN_PATCH_DESCENDANT = "genPatchDescendant"
    const val GEN_INVOKE = "genInvoke"
    const val GEN_INVOKE_SUSPEND = "genInvokeSuspend"

    const val CREATE = "create"
    const val MOUNT = "mount"
    const val GEN_PATCH_INTERNAL = "genPatchInternal"

    const val HAVE_TO_PATCH = "haveToPatch"
    const val GET_CREATE_CLOSURE_DIRTY_MASK = "getCreateClosureDirtyMask"
    const val GET_THIS_CLOSURE_DIRTY_MASK = "getThisClosureDirtyMask"
    const val SET_STATE_VARIABLE = "setStateVariable"
    const val GET_CREATE_CLOSURE_VARIABLE = "getCreateClosureVariable"
    const val GET_THIS_CLOSURE_VARIABLE = "getThisClosureVariable"
    const val INVALID_INDEX = "invalidIndex"

    const val ADAPTER = "adapter"
    const val PARENT = "parent"
    const val INDEX = "index"
    const val STATE = "state"
    const val SUPPORT_FUNCTION_INDEX = "supportFunctionIndex"
    const val SUPPORT_FUNCTION_RECEIVING_FRAGMENT = "receivingFragment"

    const val HELPER_ADAPTER = "adapter"
    const val HELPER_FRAGMENT = "fragment"
    const val HELPER_THIS_STATE = "thisState"

    const val SUPPORT_FUNCTION_INVOKE = "invoke"

    const val BT = "BT" // type parameter for fragment, Bridge Type
    const val ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter

    const val KOTLIN_INVOKE = "invoke"
}

object Names {
    val PARENT = Name.identifier(Strings.PARENT)
    val INDEX = Name.identifier(Strings.INDEX)

    val ADAPTER = Name.identifier(Strings.ADAPTER)

    val HELPER_ADAPTER = Name.identifier(Strings.HELPER_ADAPTER)
    val HELPER_FRAGMENT = Name.identifier(Strings.HELPER_FRAGMENT)
    val HELPER_THIS_STATE = Name.identifier(Strings.HELPER_THIS_STATE)

    val BT = Name.identifier(Strings.BT)

    val KOTLIN_INVOKE = Name.identifier(Strings.KOTLIN_INVOKE)

    val PROPERTY_METADATA = Name.identifier(Strings.PROPERTY_METADATA)
}

object FqNames {
    val String.runtime
        get() = FqName(Strings.RUNTIME_PACKAGE + "." + this)

    val ADAPTIVE_PACKAGE = FqName(Strings.RUNTIME_PACKAGE)
    val META_PACKAGE = FqName(Strings.META_PACKAGE)

    val ADAPTIVE_NAMESPACE = Strings.ADAPTIVE_NAMESPACE.runtime
    val ADAPTIVE_CLOSURE = Strings.ADAPTIVE_CLOSURE.runtime
    val ADAPTIVE_FRAGMENT = Strings.ADAPTIVE_FRAGMENT.runtime
    val ADAPTIVE_ADAPTER = Strings.ADAPTIVE_ADAPTER.runtime
    val ADAPTIVE_SEQUENCE = Strings.ADAPTIVE_SEQUENCE.runtime
    val ADAPTIVE_SELECT = Strings.ADAPTIVE_SELECT.runtime
    val ADAPTIVE_LOOP = Strings.ADAPTIVE_LOOP.runtime
    val ADAPTIVE_ENTRY_FUNCTION = Strings.ENTRY_FUNCTION.runtime
    val ADAPTIVE_ANONYMOUS = Strings.ADAPTIVE_ANONYMOUS.runtime
    val ADAPTIVE_FRAGMENT_FACTORY = Strings.ADAPTIVE_FRAGMENT_FACTORY.runtime
    val ADAPTIVE_SUPPORT_FUNCTION = Strings.ADAPTIVE_SUPPORT_FUNCTION.runtime
    val ADAPTIVE_STATE_VARIABLE_BINDING = Strings.ADAPTIVE_STATE_VARIABLE_BINDING.runtime
    val ADAPTIVE_STATE_API = Strings.ADAPTIVE_STATE_API.runtime

}

object ClassIds {
    val PROPERTY_METADATA = ClassId(FqNames.META_PACKAGE, Names.PROPERTY_METADATA)
}

object CallableIds {
    val HELPER_FUNCTION_ADAPTER = CallableId(FqNames.ADAPTIVE_PACKAGE, Names.HELPER_ADAPTER)
    val HELPER_FUNCTION_FRAGMENT = CallableId(FqNames.ADAPTIVE_PACKAGE, Names.HELPER_FRAGMENT)
    val HELPER_FUNCTION_THIS_STATE = CallableId(FqNames.ADAPTIVE_PACKAGE, Names.HELPER_THIS_STATE)
}

object Indices {

    /**
     * Fragment constructor arguments.
     */
    const val ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT = 3
    const val ADAPTIVE_FRAGMENT_ARGUMENT_COUNT = 4
    const val ADAPTIVE_ANONYMOUS_FRAGMENT_ARGUMENT_COUNT = 5

    const val ADAPTIVE_FRAGMENT_ADAPTER = 0
    const val ADAPTIVE_FRAGMENT_PARENT = 1
    const val ADAPTIVE_FRAGMENT_INDEX = 2
    const val ADAPTIVE_FRAGMENT_STATE_SIZE = 3
    const val ADAPTIVE_FRAGMENT_FACTORY = 4

    /**
     * `build(parent, declarationIndex)` arguments
     */
    const val BUILD_PARENT = 0
    const val BUILD_DECLARATION_INDEX = 1

    /**
     * `patchDescendant(fragment)` arguments
     */
    const val PATCH_DESCENDANT_FRAGMENT = 0

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
     * Bridge type parameter for classes.
     */
    const val ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE = 0

    /**
     * AdaptiveFragmentFactory constructor arguments
     */
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT = 2

    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT = 0
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX = 1

    /**
     * AdaptiveSupportFunction constructor arguments
     */
    const val ADAPTIVE_SUPPORT_FUNCTION_ARGUMENT_COUNT = 3

    const val ADAPTIVE_SUPPORT_FUNCTION_DECLARING_FRAGMENT = 0
    const val ADAPTIVE_SUPPORT_FUNCTION_RECEIVING_FRAGMENT = 1
    const val ADAPTIVE_SUPPORT_FUNCTION_INDEX = 2

    /**
     * AdaptiveStateVariableBinding constructor arguments
     */
    const val ADAPTIVE_STATE_VARIABLE_BINDING_ARGUMENT_COUNT = 6

    const val ADAPTIVE_STATE_VARIABLE_BINDING_OWNER = 0
    const val ADAPTIVE_STATE_VARIABLE_BINDING_INDEX_IN_STATE = 1
    const val ADAPTIVE_STATE_VARIABLE_BINDING_INDEX_IN_CLOSURE = 2
    const val ADAPTIVE_STATE_VARIABLE_BINDING_SUPPORT_FUNCTION = 3
    const val ADAPTIVE_STATE_VARIABLE_BINDING_METADATA = 4
    const val ADAPTIVE_STATE_VARIABLE_BINDING_CALLBACK = 5

    /**
     * PropertyMetadata constructor arguments
     */
    const val ADAPTIVE_PROPERTY_METADATA_ARGUMENT_COUNT = 1

    const val ADAPTIVE_PROPERTY_METADATA_TYPE = 0

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