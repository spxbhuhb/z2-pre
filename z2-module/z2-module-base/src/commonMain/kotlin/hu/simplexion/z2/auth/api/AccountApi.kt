package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.Service

interface AccountApi : Service {

    suspend fun list() : List<AccountPrivate>

    suspend fun add(account : AccountPrivate, credentials: AccountCredentials, roles : List<UUID<Role>>) : UUID<AccountPrivate>

    suspend fun get(uuid: UUID<AccountPrivate>) : AccountPrivate

    suspend fun add(credentials: AccountCredentials)

    suspend fun status(uuid : UUID<AccountPrivate>) : AccountStatus

    suspend fun lock(uuid: UUID<AccountPrivate>)

    suspend fun unlock(uuid: UUID<AccountPrivate>)

    suspend fun getPolicy() : SecurityPolicy

    suspend fun changePolicy(policy: SecurityPolicy)

}