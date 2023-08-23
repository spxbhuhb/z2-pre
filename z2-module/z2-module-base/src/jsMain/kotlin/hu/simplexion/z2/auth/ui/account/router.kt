package hu.simplexion.z2.auth.ui.account

import hu.simplexion.z2.auth.ui.icons
import hu.simplexion.z2.auth.ui.strings
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object accountsRouter : NavRouter(strings.accounts, icons.accounts) {

    override var useParentNav = true

    override val default: Z2Builder = { list() }
}