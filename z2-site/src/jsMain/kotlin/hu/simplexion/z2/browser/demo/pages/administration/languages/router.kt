package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object languagesRouter : NavRouter() {
    override val label = strings.languages
    override val icon = icons.languages

    override var useParentNav = true

    override val default: Z2Builder = { list() }
}