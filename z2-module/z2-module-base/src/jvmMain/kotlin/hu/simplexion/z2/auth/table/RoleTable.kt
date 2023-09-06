package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.exposed.SchematicUuidTable
import org.jetbrains.exposed.sql.select

open class RoleTable : SchematicUuidTable<Role>(
    "auth_role",
    Role()
) {

    companion object {
        val roleTable = RoleTable()
    }

    val contextName = varchar("contextName", 50).nullable()
    val programmaticName = varchar("programmaticName", 100)
    val displayName = varchar("displayName", 50)
    val group = bool("group")

    fun getByName(name : String) : Role =
        select { programmaticName eq name }
            .map { it.toSchematic(this, Role()) }
            .single()

    fun getByContext(name : String) : List<Role> =
        select { contextName eq name }
            .map { it.toSchematic(this, Role()) }

}