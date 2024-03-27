/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.util.placeholder

/**
 * Entry point of an Adaptive component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
fun adaptive(
    @Suppress("UNUSED_PARAMETER")
    block: Adaptive.(adaptiveAdapter: AdaptiveAdapter<*>) -> Unit
) {
    placeholder() // this code is replaced by the compiler plugin
}

/**
 * Entry point of an Adaptive component tree with a specific adapter. The adapter
 * registry is not accessed in this case but the components will use the
 * adapter passed.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
fun <BT> adaptive(
    @Suppress("UNUSED_PARAMETER")
    adaptiveAdapter: AdaptiveAdapter<BT>,
    @Suppress("UNUSED_PARAMETER")
    block: Adaptive.(adaptiveAdapter: AdaptiveAdapter<BT>) -> Unit
) {
    placeholder() // this code is replaced by the compiler plugin
}