package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.impl.AuthAdminImpl.Companion.authAdminImpl
import hu.simplexion.z2.auth.impl.PrincipalImpl.Companion.principalImpl
import hu.simplexion.z2.auth.impl.RoleImpl.Companion.roleImpl
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.auth.table.CredentialsTable.Companion.credentialsTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleGroupTable.Companion.roleGroupTable
import hu.simplexion.z2.auth.table.RoleTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.localization.text.commonStrings
import org.jetbrains.exposed.sql.transactions.transaction

internal val securityPolicy = SecurityPolicy() // FIXME read policy from DB

lateinit var securityOfficerRole: Role
lateinit var securityOfficerUuid: UUID<Principal>
lateinit var anonymousUuid: UUID<Principal>

const val securityOfficerRoleName = "security-officer"
const val securityOfficerPrincipalName = "so"
const val anonymousPrincipalName = "anonymous"

fun authJvm(initialSoPassword: String = System.getenv("Z2_INITIAL_SO_PASSWORD") ?: "so") {

    tables(
        principalTable,
        credentialsTable,
        roleTable,
        roleGrantTable,
        roleGroupTable,
        sessionTable
    )

    securityOfficerRole = getOrMakeSecurityOfficerRole()
    securityOfficerUuid = getOrMakePrincipal(securityOfficerPrincipalName, initialSoPassword)
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

    val role = transaction {
        roleTable.list().firstOrNull { it.programmaticName == securityOfficerRoleName }
    } ?: Role()

    if (role.uuid == UUID.NIL) {
        transaction {
            role.programmaticName = "security-officer"
            role.displayName = "Security Officer"
            role.uuid = roleTable.insert(role)
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