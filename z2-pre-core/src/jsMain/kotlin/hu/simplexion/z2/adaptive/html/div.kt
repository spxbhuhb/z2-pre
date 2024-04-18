/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.adaptive.html

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.css.AdaptiveCssStyle
import hu.simplexion.z2.adaptive.structural.AdaptiveAnonymous
import kotlinx.browser.document
import org.w3c.dom.Node

@Suppress("UNUSED_PARAMETER")
fun Adaptive.div(vararg styles : AdaptiveCssStyle, builder : Adaptive.() -> Unit) {
    builder()
}

@Suppress("unused")
class AdaptiveDiv(
    adapter: AdaptiveAdapter<Node>,
    parent : AdaptiveFragment<Node>,
    index : Int
) : AdaptiveDOMNodeFragment(adapter, parent, index, 2, false) {

    override val receiver = document.createElement("div")

    val styles
        get() = getStyles(0)

    val builder
        get() = getFragmentFactory(1)

    override fun genBuild(parent: AdaptiveFragment<Node>, declarationIndex: Int): AdaptiveFragment<Node> =
        AdaptiveAnonymous(adapter, parent, declarationIndex, 0, builder).also { it.create() }

    override fun genPatchInternal() {
        if (firstTimeInit) {
            addClass(styles)
        }
    }

}