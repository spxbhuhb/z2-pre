package hu.simplexion.z2.auth.ui.role

import hu.simplexion.z2.auth.ui.authIcons
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object rolesRouter : NavRouter(authStrings.roles, authIcons.roles, true, { list() })