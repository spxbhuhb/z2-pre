/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

/**
 * Represents a fragment in the rendering of a Adaptive function. Adaptive components are
 * fragments themselves.
 *
 * *Internals*
 *
 * The [adaptiveExternalPatch] property is tricky. It is actually part of the parent fragment
 * as it patches the external state variables of this fragment from the state variables
 * of the parent fragment. It is nigh impossible to have this functionality in a class method
 * as it depends on the actual, exact use of the fragment. On the other hand, creating an
 * object/inner class for each use would be real waste of resources.
 *
 * The parameter of [adaptiveExternalPatch] is the fragment itself. I've decided to go with a
 * parameter instead of an extension function to make code generation easier (I think it
 * is easier this way, isn't it?)
 *
 * @param BT Bridge type.
 *
 * @property adaptiveParent The direct parent of this fragment in the Adaptive fragment tree.
 * @property adaptiveClosure For `AdaptiveAnonymous` instances the fragment that is the *start scope*.
 *                    For everything else it is null.
 */
interface AdaptiveFragment<BT> {

    val adaptiveAdapter: AdaptiveAdapter<BT>

    val adaptiveParent: AdaptiveFragment<BT>?

    val adaptiveClosure: AdaptiveClosure<BT>?

    val adaptiveExternalPatch: AdaptiveExternalPatchType<BT>

    val adaptiveState: Array<Any?>

    fun adaptiveCreate()
    fun adaptiveMount(bridge: AdaptiveBridge<BT>)
    fun adaptivePatch()
    fun adaptiveUnmount(bridge: AdaptiveBridge<BT>)
    fun adaptiveDispose()

    fun adaptiveExportState(): Array<Any?> = TODO()
    fun adaptiveImportState(state: Array<Any?>): Unit = TODO()

}