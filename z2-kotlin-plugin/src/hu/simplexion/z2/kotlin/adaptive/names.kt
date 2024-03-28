/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object Strings {
    const val RUNTIME_PACKAGE = "hu.simplexion.z2.adaptive"

    const val ADAPTIVE_NAMESPACE = "Adaptive"
    const val ADAPTIVE_ENTRY_FUNCTION = "hu.simplexion.z2.adaptive.adaptive"

    const val ADAPTIVE_ROOT = "AdaptiveRoot"
    const val ADAPTIVE_ANONYMOUS = "AdaptiveAnonymous"
    const val ADAPTIVE_FRAGMENT = "AdaptiveFragment"
    const val ADAPTIVE_CLOSURE = "AdaptiveClosure"
    const val ADAPTIVE_GENERATED_FRAGMENT = "AdaptiveGeneratedFragment"
    const val ADAPTIVE_ADAPTER = "AdaptiveAdapter"
    const val ADAPTIVE_BRIDGE = "AdaptiveBridge"
    const val ADAPTIVE_SEQUENCE = "AdaptiveSequence"
    const val ADAPTIVE_SELECT = "AdaptiveSelect"
    const val ADAPTIVE_LOOP = "AdaptiveLoop"
    const val ADAPTIVE_SUPPORT_FUNCTION = "AdaptiveSupportFunction"

    const val BUILD = "build"
    const val PATCH = "patch"
    const val INVOKE = "invoke"

    const val CREATE = "create"
    const val MOUNT = "mount"
    const val UNMOUNT = "unmount"
    const val DISPOSE = "dispose"

    const val GET_CLOSURE_VARIABLE = "getClosureVariable"

    const val PARENT = "parent"
    const val INDEX = "index"
    const val DECLARATION_INDEX = "declarationIndex"
    const val FRAGMENT = "fragment"
    const val SUPPORT_FUNCTION = "supportFunction"
    const val ARGUMENTS = "arguments"
    const val VARIABLE_INDEX = "variableIndex"

    const val ID = "id"
    const val ADAPTER = "adapter"
    const val CREATE_CLOSURE = "thisClosure"
    const val THIS_CLOSURE = "thisClosure"
    const val CONTAINED_FRAGMENT = "containedFragment"
    const val DIRTY_MASK = "dirtyMask"

    const val TRACE = "trace" // name of the trace function in the adapter class

    const val BT = "BT" // type parameter for fragment, Bridge Type
    const val ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter

    fun String.toNameWithPostfix(postfix: Int) =
        Name.identifier("$this$postfix")

    fun String.toNameWithPostfix(postfix: String) =
        Name.identifier("$this$postfix")
}

object Names {
    val PARENT = Name.identifier(Strings.PARENT)
    val INDEX = Name.identifier(Strings.INDEX)

    val ADAPTER = Name.identifier(Strings.ADAPTER)
    val THIS_CLOSURE = Name.identifier(Strings.THIS_CLOSURE)
    val CREATE_CLOSURE = Name.identifier(Strings.CREATE_CLOSURE)
    val CONTAINED_FRAGMENT = Name.identifier(Strings.CONTAINED_FRAGMENT)
    val DIRTY_MASK = Name.identifier(Strings.DIRTY_MASK)

    val BT = Name.identifier(Strings.BT)

    val BUILD = Name.identifier(Strings.BUILD)
    val PATCH = Name.identifier(Strings.PATCH)
    val INVOKE = Name.identifier(Strings.INVOKE)

    val CREATE = Name.identifier(Strings.CREATE)
    val MOUNT = Name.identifier(Strings.MOUNT)
    val UNMOUNT = Name.identifier(Strings.UNMOUNT)
    val DISPOSE = Name.identifier(Strings.DISPOSE)

    val DECLARATION_INDEX = Name.identifier(Strings.DECLARATION_INDEX)
    val FRAGMENT = Name.identifier(Strings.FRAGMENT)
    val SUPPORT_FUNCTION = Name.identifier(Strings.SUPPORT_FUNCTION)
    val ARGUMENTS = Name.identifier(Strings.ARGUMENTS)
}

object FqNames {
    val String.runtime
        get() = FqName(Strings.RUNTIME_PACKAGE + "." + this)

    val ADAPTIVE_NAMESPACE = Strings.ADAPTIVE_NAMESPACE.runtime
    val ADAPTIVE_CLOSURE = Strings.ADAPTIVE_CLOSURE.runtime
    val ADAPTIVE_FRAGMENT = Strings.ADAPTIVE_FRAGMENT.runtime
    val ADAPTIVE_GENERATED_FRAGMENT = Strings.ADAPTIVE_GENERATED_FRAGMENT.runtime
    val ADAPTIVE_ADAPTER = Strings.ADAPTIVE_ADAPTER.runtime
    val ADAPTIVE_BRIDGE = Strings.ADAPTIVE_BRIDGE.runtime
    val ADAPTIVE_SEQUENCE = Strings.ADAPTIVE_SEQUENCE.runtime
    val ADAPTIVE_SELECT = Strings.ADAPTIVE_SELECT.runtime
    val ADAPTIVE_FOR_LOOP = Strings.ADAPTIVE_LOOP.runtime
    val ADAPTIVE_ENTRY_FUNCTION = Strings.ADAPTIVE_ENTRY_FUNCTION.runtime
    val ADAPTIVE_ANONYMOUS = Strings.ADAPTIVE_ANONYMOUS.runtime
}

object Indices {

    /**
     * Fragment constructor arguments.
     *
     * 1. adapter
     * 2. parent
     * 3. index
     */
    const val ADAPTIVE_FRAGMENT_ARGUMENT_COUNT = 4

    const val ADAPTIVE_FRAGMENT_ADAPTER = 0
    const val ADAPTIVE_FRAGMENT_PARENT = 1
    const val ADAPTIVE_FRAGMENT_INDEX = 2

    /**
     * Builder function arguments.
     *
     *
     */
    const val ADAPTIVE_BUILDER_ARGUMENT_COUNT = 2

    const val ADAPTIVE_BUILDER_PARENT = 0
    const val ADAPTIVE_BUILDER_DECLARATION_INDEX = 1

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
}