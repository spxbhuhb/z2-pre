package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.Credentials
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.model.SecurityPolicy
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.Service

interface PrincipalApi : Service {

    suspend fun list(): List<Principal>

    suspend fun add(principal: Principal, activated: Boolean, activationKey: String?, roles: List<UUID<Role>>): UUID<Principal>

    suspend fun add(credentials: Credentials, currentCredentials: Credentials? = null)

    suspend fun get(uuid: UUID<Principal>): Principal

    suspend fun activate(credentials: Credentials, activationKey: Credentials)

    suspend fun setActivated(uuid: UUID<Principal>, activated : Boolean)

    suspend fun setLocked(uuid: UUID<Principal>, locked : Boolean)

    suspend fun getPolicy(): SecurityPolicy

    suspend fun changePolicy(policy: SecurityPolicy)

}