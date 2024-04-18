package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.api.AuthAdminApi
import hu.simplexion.z2.auth.api.PrincipalApi
import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.services.getService

val authAdminService = getService<AuthAdminApi>()
val principalService = getService<PrincipalApi>()
val roleService = getService<RoleApi>()
val sessionService = getService<SessionApi>()

fun authJs() {
    authCommon()
}