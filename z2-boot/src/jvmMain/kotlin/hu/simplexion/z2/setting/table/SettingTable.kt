package hu.simplexion.z2.setting.table

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.setting.model.Setting
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

open class SettingTable : Table(
    "z2_setting"
) {

    companion object {
        val settingTable = SettingTable()
    }

    val owner = reference("owner", principalTable).index()
    val path = varchar("path", 200).index()
    val value = text("value").nullable()

    fun put(inOwner: UUID<Principal>, inPath: String, inValue: String?) {
        deleteWhere { (owner eq inOwner.jvm) and ((path eq inPath) or (path like "$inPath/%")) }
        insert {
            it[owner] = inOwner.jvm
            it[path] = inPath
            it[value] = inValue
        }
    }

    fun get(inOwner: UUID<Principal>, inPath: String, children: Boolean): List<Setting> {
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