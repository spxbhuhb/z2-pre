package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter
import hu.simplexion.z2.setting.ui.settingIcons
import hu.simplexion.z2.setting.ui.settingStrings

@Suppress("unused")
object languagesRouter : NavRouter() {
    override val label = settingStrings.languages
    override val icon = settingIcons.languages

    override var useParentNav = true

    override val default: Z2Builder = { list() }
}