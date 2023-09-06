package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.jvm
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

open class RoleGroupTable : Table("auth_role_group") {

    companion object {
        val roleGroupTable = RoleGroupTable()
    }

    val group = reference("group", roleTable)
    val item = reference("item", roleTable)

    fun add(inItem: UUID<Role>, inGroup: UUID<Role>) {
        if (select { (item eq inItem.jvm) and (group eq inGroup.jvm) }.count() > 0) return
        insert {
            it[group] = inGroup.jvm
            it[item] = inItem.jvm
        }
    }

    fun remove(inItem: UUID<Role>, inGroup: UUID<Role>) {
        deleteWhere { (item eq inItem.jvm) and (group eq inGroup.jvm) }
    }

}