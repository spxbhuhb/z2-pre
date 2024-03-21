/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveLoop<BT, IT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val closure: AdaptiveClosure<BT>,
    override val parent: AdaptiveFragment<BT>,
    val makeIterator: () -> Iterator<IT>,
    val makeFragment: (parent : AdaptiveFragment<BT>) -> AdaptiveFragment<BT>
) : AdaptiveFragment<BT> {

    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT> = { }
    override val state: Array<Any?> = emptyArray()

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
        placeholder = adapter.createPlaceholder()
        bridge.add(placeholder)

        for (fragment in fragments) {
            fragment.adaptiveMount(placeholder)
        }
    }

    override fun adaptiveInternalPatch() {
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
                    it.adaptiveInternalPatch()
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