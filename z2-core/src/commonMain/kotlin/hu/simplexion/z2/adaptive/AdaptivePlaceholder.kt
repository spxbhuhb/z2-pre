/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

@Suppress("unused")
class AdaptivePlaceholder<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>,
) : AdaptiveStructuralFragment<BT> {

    override val id = adapter.newId()

    override val index: Int
        get() = shouldNotRun()

    override val state: Array<Any?> = emptyArray()

    override var dirtyMask: Int
        get() = shouldNotRun()
        set(@Suppress("UNUSED_PARAMETER") v) {
            shouldNotRun()
        }

    val bridge = adapter.createPlaceholder()

    override fun create() {

    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        bridge.add(this.bridge)
    }

    override fun patchInternal() {

    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        bridge.remove(this.bridge)
    }

    override fun dispose() {

    }

}