package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.Service

interface SessionApi : Service {

    suspend fun owner() : UUID<AccountPrivate>?

    suspend fun roles() : List<String> // FIXME, use role list instead of string

    suspend fun login(name : String, password : String) : Int

    suspend fun logout()

    suspend fun logout(session : UUID<Session>)

    suspend fun list() : List<Session>

}