package hu.simplexion.z2.auth

import hu.simplexion.z2.application.SECURITY_OFFICER_PRINCIPAL_NAME
import hu.simplexion.z2.application.SECURITY_OFFICER_ROLE_UUID
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
import hu.simplexion.z2.auth.table.RoleTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.util.UUID
import org.jetbrains.exposed.sql.transactions.transaction

lateinit var securityOfficerRole: Role
lateinit var securityOfficerUuid: UUID<Principal>
lateinit var anonymousUuid: UUID<Principal>

const val anonymousPrincipalName = "anonymous"

fun authJvm() {

    tables(
        principalTable,
        credentialsTable,
        roleTable,
        roleGrantTable,
        roleGroupTable,
        sessionTable
    )

    securityOfficerRole = getOrMakeSecurityOfficerRole()
    securityOfficerUuid = getOrMakePrincipal(SECURITY_OFFICER_PRINCIPAL_NAME, "so")
    anonymousUuid = getOrMakePrincipal(anonymousPrincipalName)

    grantRole(securityOfficerRole, securityOfficerUuid)

    implementations(
        principalImpl,
        roleImpl,
        sessionImpl,
        authAdminImpl
    )
}

private fun getOrMakeSecurityOfficerRole(): Role {
    val roleTable = RoleTable()

    val role = transaction { roleTable.getOrNull(SECURITY_OFFICER_ROLE_UUID) } ?: Role()

    if (role.uuid == UUID.NIL) {
        transaction {
            role.uuid = SECURITY_OFFICER_ROLE_UUID
            role.programmaticName = "security-officer"
            role.displayName = "Security Officer"
            roleTable.insertWithId(role)
        }
    }

    return role
}

internal fun getOrMakePrincipal(
    name: String,
    password: String? = null
): UUID<Principal> {
    val principal = transaction {
        principalTable.getByNameOrNull(name)
    }
    if (principal != null) return principal.uuid

    return transaction {

        val principalId = principalTable.insert(
            Principal().also {
                it.name = name
                it.activated = true
                it.locked = (password == null)
            }
        )

        if (password != null) {
            credentialsTable.insert(
                Credentials().also {
                    it.principal = principalId
                    it.type = CredentialType.PASSWORD
                    it.value = BCrypt.hashpw(password, BCrypt.gensalt())
                }
            )
        }

        securityHistory(principalId, baseStrings.account, commonStrings.add, principalId, name)

        return@transaction principalId
    }
}

private fun grantRole(role: Role, principal: UUID<Principal>) {
    transaction {
        if (role.uuid in roleGrantTable.rolesOf(principal, null).map { it.uuid }) return@transaction
        roleGrantTable.insert(role.uuid, principal, null)
        securityHistory(securityOfficerUuid, baseStrings.role, baseStrings.grantRole, securityOfficerUuid, principal, role.uuid, role.programmaticName, role.displayName)
    }
}