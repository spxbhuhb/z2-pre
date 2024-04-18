package hu.simplexion.z2.auth.ui.role

import hu.simplexion.z2.baseIcons
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.routing.NavRouter

val RoleList = NavRouter(baseStrings.roles, baseIcons.roles, useParentNav =  true) { list() }