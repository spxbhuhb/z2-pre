package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object languagesRouter : NavRouter() {
    override val label = i18nStrings.languages
    override val icon = i18nIcons.languages

    override var useParentNav = true

    override val default: Z2Builder = { list() }
}