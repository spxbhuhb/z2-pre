package hu.simplexion.z2.auth.tables

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.exposed.SchematicUuidTable
import org.jetbrains.exposed.sql.select

open class AccountPrivateTable : SchematicUuidTable<AccountPrivate>(
    "auth_account_private",
    AccountPrivate()
) {

    val accountName = varchar("accountName", 50).uniqueIndex()
    val fullName = varchar("fullName", 100)
    val email = varchar("email", 264)
    val phone = varchar("phone", 25).nullable()

    fun getByAccountNameOrNull(inAccountName : String) : AccountPrivate? =
        select { accountName eq inAccountName }
            .map {
                it.toSchematic(this, newInstance())
            }
            .firstOrNull()

}