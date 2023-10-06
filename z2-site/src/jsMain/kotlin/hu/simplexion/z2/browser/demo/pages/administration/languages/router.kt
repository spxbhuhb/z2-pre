package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.browser.demo.icons
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

object LanguagesList : NavRouter(strings.languages, icons.languages) {
    override var useParentNav = true
    override var default: Z2Builder = { list() }
}