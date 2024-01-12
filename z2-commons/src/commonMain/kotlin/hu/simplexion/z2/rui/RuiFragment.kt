/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

/**
 * Represents a fragment in the rendering of a Rui function. Rui components are
 * fragments themselves.
 *
 * *Internals*
 *
 * The [ruiExternalPatch] property is tricky. It is actually part of the parent fragment
 * as it patches the external state variables of this fragment from the state variables
 * of the parent fragment. It is nigh impossible to have this functionality in a class method
 * as it depends on the actual, exact use of the fragment. On the other hand, creating an
 * object/inner class for each use would be real waste of resources.
 *
 * The parameter of [ruiExternalPatch] is the fragment itself. I've decided to go with a
 * parameter instead of an extension function to make code generation easier (I think it
 * is easier this way, isn't it?)
 *
 * @param BT Bridge type.
 *
 * @property ruiScope Parent scope of this fragment. Each original function and each
 *                    anonymous rendering function starts a new scope. These scopes
 *                    define a tree during runtime which is used for getting state
 *                    variables and patching.
 *
 * @property ruiStateSize               Number of state variables in this fragment (external and internal together).
 * @property ruiCallSiteDependencyMask  Represents the dependency of the given fragment on state variables of the scope.
 */
interface RuiFragment<BT> {

    val ruiAdapter: RuiAdapter<BT>
    val ruiScope: RuiFragment<BT>?
    val ruiExternalPatch: RuiExternalPathType<BT>

    val ruiStateSize : Int
    val ruiCallSiteDependencyMask : RuiStateVariableMask

    fun ruiCreate()
    fun ruiMount(bridge: RuiBridge<BT>)
    fun ruiPatch(dirtyMaskOfScope: RuiStateVariableMask)
    fun ruiUnmount(bridge: RuiBridge<BT>)
    fun ruiDispose()

    fun ruiExportState(): Array<Any?> = TODO()
    fun ruiImportState(state: Array<Any?>): Unit = TODO()

}