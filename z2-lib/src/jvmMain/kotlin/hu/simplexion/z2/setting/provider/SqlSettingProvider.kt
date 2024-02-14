package hu.simplexion.z2.setting.provider

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.setting.persistence.SettingTable
import hu.simplexion.z2.util.UUID
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Stores settings in the SQL table [table].
 *
 * [get] silently returns with an empty list while [isSqlInitialized] is false.
 */
class SqlSettingProvider(
    val table: SettingTable
) : SettingProvider {

    override val isReadOnly: Boolean
        get() = false

    override fun put(owner: UUID<Principal>, path: String, value: String?) {
        table.put(owner, path, value)
    }

    override fun get(owner: UUID<Principal>, path: String, children: Boolean): List<Setting> =
        transaction {
            table.get(owner, path, children)
        }


}