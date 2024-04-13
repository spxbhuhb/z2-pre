/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.adaptive.html

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveAnonymous
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.css.AdaptiveCssStyle
import hu.simplexion.z2.adaptive.css.displayGrid
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Node

@Suppress("UNUSED_PARAMETER")
fun Adaptive.grid(templateColumns: String, templateRows: String, vararg styles: AdaptiveCssStyle, builder: Adaptive.() -> Unit) {
    builder()
}

@Suppress("unused")
class AdaptiveGrid(
    adapter: AdaptiveAdapter<Node>,
    parent: AdaptiveFragment<Node>,
    index: Int
) : AdaptiveDOMNodeFragment(adapter, parent, index, 4, false) {

    override val receiver = document.createElement("div") as HTMLDivElement

    val templateColumns get() = state[0] as String
    val templateRows get() = state[1] as String
    val styles get() = getStyles(2)
    val builder get() = getFragmentFactory(3)

    override fun genBuild(parent: AdaptiveFragment<Node>, declarationIndex: Int): AdaptiveFragment<Node> =
        AdaptiveAnonymous(adapter, parent, declarationIndex, 0, builder).also { it.create() }

    override fun genPatchInternal() {
        if (firstTimeInit) {
            receiver.style.setProperty("grid-template-columns", templateColumns)
            receiver.style.setProperty("grid-template-rows", templateRows)
            addClass(styles + displayGrid)
        }
    }

}