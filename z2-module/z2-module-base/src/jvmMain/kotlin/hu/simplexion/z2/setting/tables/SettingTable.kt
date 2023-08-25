package hu.simplexion.z2.setting.tables

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.tables.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.setting.model.Setting
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

open class SettingTable : Table(
    "setting"
) {

    companion object {
        val settingTable = SettingTable()
    }

    val owner = reference("owner", accountPrivateTable).index()
    val path = varchar("path", 200).index()
    val value = text("value").nullable()

    fun put(inOwner: UUID<AccountPrivate>, inPath: String, inValue: String?) {
        deleteWhere { (owner eq inOwner.jvm) and (path like "$inPath%") }
        insert {
            it[owner] = inOwner.jvm
            it[path] = inPath
            it[value] = inValue
        }
    }

    fun get(inOwner: UUID<AccountPrivate>, inPath: String, children: Boolean): List<Setting> {
        val pathCondition = if (children) {
            ((path eq inPath) or (path like "$inPath/%"))
        } else {
            (path eq inPath)
        }

        val result =
            slice(path, value)
                .select { (owner eq inOwner.jvm) and pathCondition }
                .map { row ->
                    Setting().also {
                        it.path = row[path]
                        it.value = row[value]
                    }
                }

        return result
    }

}