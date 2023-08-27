package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.AccountCredentials
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select

open class AccountCredentialsTable(
    accountPrivateTable: AccountPrivateTable
) : SchematicUuidTable<AccountCredentials>(
    "auth_account_credential",
    AccountCredentials(),
) {

    companion object {
        val accountCredentialsTable = AccountCredentialsTable(accountPrivateTable)
    }

    val account = reference("account", accountPrivateTable)
    val type = varchar("type", 50)
    val value = text("value")
    val createdAt = timestamp("createdAt")

    fun readValue(inAccount: UUID<AccountPrivate>, inType: String): String? =
        slice(value)
            .select {
                (account eq inAccount.jvm) and (type eq inType)
            }
            .orderBy(createdAt, SortOrder.DESC)
            .limit(1)
            .firstOrNull()
            ?.let { it[value] }

}