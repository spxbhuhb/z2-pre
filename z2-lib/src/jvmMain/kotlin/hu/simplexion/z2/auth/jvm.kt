package hu.simplexion.z2.auth

import hu.simplexion.z2.application.ApplicationSettings
import hu.simplexion.z2.auth.impl.AuthAdminImpl.Companion.authAdminImpl
import hu.simplexion.z2.auth.impl.PrincipalImpl.Companion.principalImpl
import hu.simplexion.z2.auth.impl.RoleImpl.Companion.roleImpl
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.auth.model.CredentialType
import hu.simplexion.z2.auth.model.Credentials
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.table.CredentialsTable.Companion.credentialsTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleGroupTable.Companion.roleGroupTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.exposed.*
import hu.simplexion.z2.util.UUID
import org.jetbrains.exposed.sql.transactions.transaction

fun authJvm() {

    val firstTimeInit = isAllEmpty(
        *tables(
            principalTable,
            credentialsTable,
            roleTable,
            roleGrantTable,
            roleGroupTable,
            sessionTable
        )
    )

    transaction {
        with(ApplicationSettings) {
            if (firstTimeInit) {
                makePrincipal(securityOfficerUuid, securityOfficerName, password = securityOfficerName)

                makeRole(securityOfficerRoleUuid, securityOfficerRoleName)
                makeRoleGrant(securityOfficerRoleUuid, securityOfficerUuid)

                makeRole(technicalAdminRoleUuid, technicalAdminRoleName)
                makeRoleGrant(securityOfficerRoleUuid, securityOfficerUuid)

                makePrincipal(anonymousUuid, anonymousName)
            } else {
                validatePrincipal(securityOfficerUuid)

                validateRole(securityOfficerRoleUuid, securityOfficerRoleName)
                validateRoleGrant(securityOfficerRoleUuid, securityOfficerUuid)

                validateRole(technicalAdminRoleUuid, technicalAdminRoleName)
                // technical admin may be revoked from the security officer

                validatePrincipal(anonymousUuid, locked = true)
            }
        }
    }

    implementations(
        principalImpl,
        roleImpl,
        sessionImpl,
        authAdminImpl
    )
}

private fun makeRole(inUuid: UUID<Role>, inName: String) {
    roleTable.insertWithId(
        Role().apply {
            uuid = inUuid
            programmaticName = inName
            displayName = inName
        }
    )
}

private fun validateRole(inUuid: UUID<Role>, inName: String) {
    requireNotNull(roleTable.getOrNull(inUuid)) { "missing mandatory role: uuid=$inUuid name=$inName" }
}

internal fun makePrincipal(inUuid: UUID<Principal>, inName: String, password: String? = null) {
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


internal fun validatePrincipal(inUuid: UUID<Principal>, locked: Boolean? = null) {
    val principal = principalTable[inUuid]
    if (locked == true) check(principal.locked) { "principal shall be locked: name=${principal.name} uuid=$inUuid" }
}


private fun makeRoleGrant(role: UUID<Role>, principal: UUID<Principal>) {
    roleGrantTable.insert(role, principal, null)
}

private fun validateRoleGrant(role: UUID<Role>, principal: UUID<Principal>) {
    check(
        roleGrantTable.hasRole(
            role,
            principal,
            null
        )
    ) { "principal $principal does not have the mandatory role $role" }
}