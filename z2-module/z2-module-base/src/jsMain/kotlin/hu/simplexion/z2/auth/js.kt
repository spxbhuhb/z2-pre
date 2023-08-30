package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.api.AccountApi
import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.service.runtime.getService

val accounts = getService<AccountApi>()
val roles = getService<RoleApi>()
val sessions = getService<SessionApi>()

fun authJs() {
    authCommon()
}