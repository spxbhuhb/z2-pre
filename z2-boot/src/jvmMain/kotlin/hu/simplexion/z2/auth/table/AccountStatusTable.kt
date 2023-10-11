package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountStatus
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
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

    val account = reference("account", accountPrivateTable).uniqueIndex()
    val activated = bool("activated")
    val locked = bool("locked")
    val expired = bool("expired")
    val anonymized = bool("anonymized")
    val lastAuthSuccess = timestamp("lastAuthSuccess").nullable()
    val authSuccessCount = integer("authSuccessCount")
    val lastAuthFail = timestamp("lastAuthFail").nullable()
    val authFailCount = integer("authFailCount")

    fun readOrNull(inAccount: UUID<AccountPrivate>): AccountStatus? =
        select { account eq inAccount }
            .map { it.toSchematic(this, newInstance()) }
            .firstOrNull()

    fun setLocked(inAccount : UUID<AccountPrivate>, inLocked : Boolean) {
        update({ account eq inAccount }) {
            it[locked] = inLocked
        }
    }

    fun setActivated(inAccount : UUID<AccountPrivate>, activated : Boolean) {
        update({ account eq inAccount }) {
            it[this.activated] = activated
        }
    }

}