package hu.simplexion.z2.auth.ui.role

import hu.simplexion.z2.auth.ui.authIcons
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object rolesRouter : NavRouter(authStrings.roles, authIcons.roles) {

    override var useParentNav = true

    override val default: Z2Builder = { list() }
}