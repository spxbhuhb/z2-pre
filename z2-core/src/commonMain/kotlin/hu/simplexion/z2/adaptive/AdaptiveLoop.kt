/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveLoop<BT, IT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>?,
    override val adaptiveParent: AdaptiveFragment<BT>,
    val makeIterator: () -> Iterator<IT>,
    val makeFragment: (parent : AdaptiveFragment<BT>) -> AdaptiveFragment<BT>
) : AdaptiveFragment<BT> {

    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT> = { }

    var loopVariable: IT? = null

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    lateinit var placeholder: AdaptiveBridge<BT>

    override fun adaptiveCreate() {
        for (loopVariable in makeIterator()) {
            this.loopVariable = loopVariable
            fragments.add(makeFragment(this))
        }
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        placeholder = adaptiveAdapter.createPlaceholder()
        bridge.add(placeholder)

        for (fragment in fragments) {
            fragment.adaptiveMount(placeholder)
        }
    }

    override fun adaptivePatch() {
        var index = 0
        for (loopVariable in makeIterator()) {
            this.loopVariable = loopVariable
            if (index >= fragments.size) {
                val f = makeFragment(this)
                fragments.add(f)
                f.adaptiveCreate()
                f.adaptiveMount(placeholder)
            } else {
                fragments[index].also {
                    it.adaptiveExternalPatch(it)
                    it.adaptivePatch()
                }
            }
            index ++
        }
        while (index < fragments.size) {
            val f = fragments.removeLast()
            f.adaptiveUnmount(placeholder)
            f.adaptiveDispose()
            index ++
        }
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        for (fragment in fragments) {
            fragment.adaptiveUnmount(placeholder)
        }
        bridge.remove(placeholder)
    }

    override fun adaptiveDispose() {
        for (f in fragments) {
            f.adaptiveDispose()
        }
    }
}