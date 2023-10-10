package hu.simplexion.z2.auth.ui.account

import hu.simplexion.z2.baseIcons
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object accountsRouter : NavRouter(baseStrings.accounts, baseIcons.accounts) {

    override var useParentNav = true

    override var default: Z2Builder = { list() }
}