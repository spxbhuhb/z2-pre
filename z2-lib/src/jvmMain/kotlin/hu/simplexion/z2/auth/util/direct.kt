package hu.simplexion.z2.auth.util

import hu.simplexion.z2.auth.model.CredentialType
import hu.simplexion.z2.auth.model.Credentials
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.table.CredentialsTable.Companion.credentialsTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.util.UUID


fun makeRole(role: Role): Role {
    roleTable.insertWithId(role)
    return role
}

fun makeRole(inUuid: UUID<Role>, inName: String): Role {
    val role = Role {
        uuid = inUuid
        programmaticName = inName
        displayName = inName
    }
    roleTable.insertWithId(role)
    return role
}

fun validateRole(role: Role) =
    requireNotNull(roleTable.getOrNull(role.uuid)) { "missing mandatory role: uuid=${role.uuid} name=${role.programmaticName}" }

fun validateRole(inUuid: UUID<Role>, inName: String) =
    requireNotNull(roleTable.getOrNull(inUuid)) { "missing mandatory role: uuid=$inUuid name=$inName" }

fun makePrincipal(inUuid: UUID<Principal>, inName: String, password: String? = null) {
    principalTable.insertWithId(
        Principal().also {
            it.uuid = inUuid
            it.name = inName
            it.activated = true
            it.locked = (password == null)
        }
    )

    if (password != null) {
        credentialsTable.insert(
            Credentials().also {
                it.principal = inUuid
                it.type = CredentialType.PASSWORD
                it.value = BCrypt.hashpw(password, BCrypt.gensalt())
            }
        )
    }
}


fun validatePrincipal(inUuid: UUID<Principal>, locked: Boolean? = null) {
    val principal = principalTable.getOrNull(inUuid)
    requireNotNull(principal) { "missing mandatory principal uuid=$inUuid" }
    if (locked == true) check(principal.locked) { "principal shall be locked: name=${principal.name} uuid=$inUuid" }
}


fun makeRoleGrant(role: UUID<Role>, principal: UUID<Principal>) {
    roleGrantTable.insert(role, principal, null)
}

fun validateRoleGrant(role: UUID<Role>, principal: UUID<Principal>) {
    check(
        roleGrantTable.hasRole(
            role,
            principal,
            null
        )
    ) { "principal $principal does not have the mandatory role $role" }
}