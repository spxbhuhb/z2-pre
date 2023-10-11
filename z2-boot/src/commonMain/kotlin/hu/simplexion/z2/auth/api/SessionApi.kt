package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.Service

interface SessionApi : Service {

    suspend fun owner() : UUID<AccountPrivate>?

    suspend fun roles() : List<Role>

    suspend fun login(name : String, password : String) : Session

    suspend fun getSession() : Session?

    suspend fun logout()

    suspend fun logout(session : UUID<Session>)

    suspend fun list() : List<Session>

}