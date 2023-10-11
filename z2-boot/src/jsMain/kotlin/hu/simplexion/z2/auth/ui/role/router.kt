package hu.simplexion.z2.auth.ui.role

import hu.simplexion.z2.baseIcons
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object rolesRouter : NavRouter(baseStrings.roles, baseIcons.roles, true, { list() })