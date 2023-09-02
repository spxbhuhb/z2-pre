package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountPublic
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.Service

interface RoleApi : Service {

    suspend fun list(): List<Role>

    suspend fun add(role: Role) : UUID<Role>

    suspend fun update(role: Role)

    suspend fun remove(uuid: UUID<Role>)

    suspend fun getByName(name : String) : Role

    suspend fun grant(role: UUID<Role>, account: UUID<AccountPrivate>, context : String?)

    suspend fun revoke(role: UUID<Role>, account: UUID<AccountPrivate>, context : String?)

    suspend fun rolesOf(account: UUID<AccountPrivate>, context : String?): List<Role>

    suspend fun grantedTo(role: UUID<Role>, context : String?): List<AccountPublic>

}