package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.api.AccountApi
import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.service.runtime.getService

val Accounts = getService<AccountApi>()
val Roles = getService<RoleApi>()
val Sessions = getService<SessionApi>()

fun authJs() {
    authCommon()
}