/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

/**
 * Entry point of a Adaptive component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
fun adaptive(block: (adaptiveAdapter: AdaptiveAdapter<*>) -> Unit) {
    block(AdaptiveAdapterRegistry.adapterFor())
}

/**
 * Entry point of a Adaptive component tree with a specific adapter. The adapter
 * registry is not accessed in this case but the components will use the
 * adapter passed.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
fun <BT> adaptive(adaptiveAdapter: AdaptiveAdapter<BT>, block: (adaptiveAdapter: AdaptiveAdapter<BT>) -> Unit) {
    block(adaptiveAdapter)
}