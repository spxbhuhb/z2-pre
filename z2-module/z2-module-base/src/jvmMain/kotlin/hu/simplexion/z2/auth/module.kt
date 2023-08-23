package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.impl.AccountImpl
import hu.simplexion.z2.auth.impl.RoleImpl
import hu.simplexion.z2.auth.impl.SessionImpl
import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.auth.tables.*
import hu.simplexion.z2.auth.ui.strings
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.registerWithTransaction
import hu.simplexion.z2.history.securityHistory
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

val accountPrivateTable = AccountPrivateTable()
val accountStatusTable = AccountStatusTable(accountPrivateTable)
val accountCredentialsTable = AccountCredentialsTable(accountPrivateTable)
val roleTable = RoleTable()
val roleGrantTable = RoleGrantTable(accountPrivateTable, roleTable)
val sessionTable = SessionTable()

internal val securityPolicy = SecurityPolicy() // FIXME read policy from DB

internal lateinit var securityOfficerRole: Role
internal lateinit var securityOfficerUuid: UUID<AccountPrivate>
internal lateinit var anonymousUuid: UUID<AccountPrivate>

const val securityOfficerRoleName = "security-officer"
const val securityOfficerAccountName = "so"
const val anonymousAccountName = "anonymous"

fun auth() {

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            accountPrivateTable,
            accountCredentialsTable,
            accountStatusTable,
            roleTable,
            roleGrantTable,
            sessionTable
        )
    }

    securityOfficerRole = getOrMakeSecurityOfficerRole()
    securityOfficerUuid = getOrMakeAccount(securityOfficerAccountName, "Security Officer", "so") // FIXME initial SO password
    anonymousUuid = getOrMakeAccount(anonymousAccountName, "Anonymous")

    grantRole(securityOfficerRole, securityOfficerUuid)

    registerWithTransaction(
        AccountImpl(),
        RoleImpl(),
        SessionImpl()
    )
}

private fun getOrMakeSecurityOfficerRole(): Role {
    val roleTable = RoleTable()

    val role = transaction {
        roleTable.list().firstOrNull { it.programmaticName == securityOfficerRoleName }
    } ?: Role()

    if (role.id == UUID.NIL) {
        transaction {
            role.programmaticName = "security-officer"
            role.displayName = "Security Officer"
            role.id = roleTable.insert(role)
        }
    }

    return role
}

private fun getOrMakeAccount(
    name: String,
    fullName: String,
    password: String? = null
): UUID<AccountPrivate> {
    val account = transaction {
        accountPrivateTable.getByAccountNameOrNull(name)
    }
    if (account != null) return account.id

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

        securityHistory(accountId, strings.addAccount, name, accountId)

        return@transaction accountId
    }
}

private fun grantRole(role: Role, account: UUID<AccountPrivate>) {
    transaction {
        if (role.id in roleGrantTable.rolesOf(account, UUID.nil()).map { it.id }) return@transaction
        roleGrantTable.insert(role.id, account, UUID.nil())
        securityHistory(securityOfficerUuid, strings.grantRole, securityOfficerUuid, role.id, role.programmaticName, role.displayName)
    }
}