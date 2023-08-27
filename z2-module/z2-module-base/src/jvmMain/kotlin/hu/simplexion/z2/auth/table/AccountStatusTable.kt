package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountStatus
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

open class AccountStatusTable(
    accountPrivateTable: AccountPrivateTable
) : SchematicUuidTable<AccountStatus>(
    "auth_account_status",
    AccountStatus()
) {

    companion object {
        val accountStatusTable = AccountStatusTable(accountPrivateTable)
    }

    val account = reference("account", accountPrivateTable)
    val validated = bool("validated")
    val locked = bool("locked")
    val expired = bool("expired")
    val anonymized = bool("anonymized")
    val lastLoginSuccess = timestamp("lastLoginSuccess").nullable()
    val loginSuccessCount = integer("loginSuccessCount")
    val lastLoginFail = timestamp("lastLoginFail").nullable()
    val loginFailCount = integer("loginFailCount")

    fun readOrNull(inAccount: UUID<AccountPrivate>): AccountStatus? =
        select { account eq inAccount.jvm }
            .map { it.toSchematic(this, newInstance()) }
            .firstOrNull()

    fun setLocked(inAccount : UUID<AccountPrivate>, inLocked : Boolean) {
        update({ account eq inAccount.jvm }) {
            it[locked] = inLocked
        }
    }

}