package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.Grant
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.Service

interface RoleApi : Service {

    suspend fun query(): List<Role>

    suspend fun add(role: Role) : UUID<Role>

    suspend fun update(role: Role)

    suspend fun remove(uuid: UUID<Role>)

    suspend fun getByName(name : String) : Role

    suspend fun grant(role: UUID<Role>, principal: UUID<Principal>, context : String?)

    suspend fun revoke(role: UUID<Role>, principal: UUID<Principal>, context : String?)

    suspend fun rolesOf(principal: UUID<Principal>, context : String?): List<Role>

    suspend fun grantedTo(role: UUID<Role>, context : String?): List<Grant>

    suspend fun addToGroup(role : UUID<Role>, group : UUID<Role>)

    suspend fun removeFromGroup(role : UUID<Role>, group : UUID<Role>)

}