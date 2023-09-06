package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.api.AccountApi
import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.service.runtime.getService

val accountService = getService<AccountApi>()
val roleService = getService<RoleApi>()
val sessionService = getService<SessionApi>()

fun authJs() {
    authCommon()
}