package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.util.UUID

var traceRouting = false

var isLoggedIn : Boolean = false

var effectiveRoles : List<UUID<Role>> = emptyList()