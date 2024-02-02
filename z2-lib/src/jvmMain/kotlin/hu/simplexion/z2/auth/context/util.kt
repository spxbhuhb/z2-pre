package hu.simplexion.z2.auth.context

import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.model.Session.Companion.SESSION_TOKEN_UUID
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.services.ServiceContext
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.get

fun ServiceContext.getSession(): Session =
    checkNotNull(getSessionOrNull()) { "missing or invalid session" }

fun ServiceContext.getSessionOrNull() =
    this[SESSION_TOKEN_UUID]

val ServiceImpl<*>.isSecurityOfficer
    get() = serviceContext.has(securityOfficerRole).isAllowed

