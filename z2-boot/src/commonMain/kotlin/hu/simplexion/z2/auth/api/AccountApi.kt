package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.Service

interface AccountApi : Service {

    suspend fun list(): List<AccountPrivate>

    suspend fun add(account: AccountPrivate, activated: Boolean, activationKey: String?, roles: List<UUID<Role>>): UUID<AccountPrivate>

    suspend fun add(credentials: AccountCredentials, currentCredentials: AccountCredentials? = null)

    suspend fun get(uuid: UUID<AccountPrivate>): AccountPrivate

    suspend fun status(uuid: UUID<AccountPrivate>): AccountStatus

    suspend fun activate(credentials: AccountCredentials, activationKey: AccountCredentials)

    suspend fun setActivated(uuid: UUID<AccountPrivate>, activated : Boolean)

    suspend fun setLocked(uuid: UUID<AccountPrivate>, locked : Boolean)

    suspend fun getPolicy(): SecurityPolicy

    suspend fun changePolicy(policy: SecurityPolicy)

}