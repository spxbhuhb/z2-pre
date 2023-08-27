package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.impl.AccountImpl.Companion.accountImpl
import hu.simplexion.z2.auth.impl.RoleImpl.Companion.roleImpl
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.auth.table.AccountCredentialsTable.Companion.accountCredentialsTable
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.auth.table.AccountStatusTable.Companion.accountStatusTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.history.util.securityHistory
import org.jetbrains.exposed.sql.transactions.transaction

internal val securityPolicy = SecurityPolicy() // FIXME read policy from DB

internal lateinit var securityOfficerRole: Role
internal lateinit var securityOfficerUuid: UUID<AccountPrivate>
internal lateinit var anonymousUuid: UUID<AccountPrivate>

const val securityOfficerRoleName = "security-officer"
const val securityOfficerAccountName = "so"
const val anonymousAccountName = "anonymous"

fun authJvm(initialSoPassword : String = "so") {

    tables(
        accountPrivateTable,
        accountCredentialsTable,
        accountStatusTable,
        roleTable,
        roleGrantTable,
        sessionTable
    )

    securityOfficerRole = getOrMakeSecurityOfficerRole()
    securityOfficerUuid = getOrMakeAccount(securityOfficerAccountName, "Security Officer", initialSoPassword)
    anonymousUuid = getOrMakeAccount(anonymousAccountName, "Anonymous")

    grantRole(securityOfficerRole, securityOfficerUuid)

    implementations(
        accountImpl,
        roleImpl,
        sessionImpl
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

internal fun getOrMakeAccount(
    name: String,
    fullName: String,
    password: String? = null
): UUID<AccountPrivate> {
    val account = transaction {
        accountPrivateTable.getByAccountNameOrNull(name)
    }
    if (account != null) return account.uuid

    return transaction {

        val accountId = accountPrivateTable.insert(
            AccountPrivate().also {
                it.accountName = name
                it.email = "$name@127.0.0.1"
                it.fullName = fullName
            }
        )

        if (password != null) {
            accountCredentialsTable.insert(
                AccountCredentials().also {
                    it.account = accountId
                    it.type = CredentialType.PASSWORD
                    it.value = BCrypt.hashpw(password, BCrypt.gensalt())
                }
            )
        }

        accountStatusTable.insert(
            AccountStatus().also {
                it.account = accountId
                it.validated = true
                it.locked = (password == null)
            }
        )

        securityHistory(accountId, authStrings.addAccount, name, accountId)

        return@transaction accountId
    }
}

private fun grantRole(role: Role, account: UUID<AccountPrivate>) {
    transaction {
        if (role.uuid in roleGrantTable.rolesOf(account, null).map { it.uuid }) return@transaction
        roleGrantTable.insert(role.uuid, account, null)
        securityHistory(securityOfficerUuid, authStrings.grantRole, securityOfficerUuid, role.uuid, role.programmaticName, role.displayName)
    }
}