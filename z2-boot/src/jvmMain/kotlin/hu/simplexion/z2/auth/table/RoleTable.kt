package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.exposed.SchematicUuidTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

open class RoleTable : SchematicUuidTable<Role>(
    "z2_auth_role",
    Role()
) {

    companion object {
        val roleTable = RoleTable()
    }

    val contextName = varchar("contextName", 50).nullable()
    val programmaticName = varchar("programmaticName", 100)
    val displayName = varchar("displayName", 50)
    val group = bool("group")
    val displayOrder = integer("displayOrder").nullable()

    fun getByName(name : String) : Role =
        select { programmaticName eq name }
            .map { it.toSchematic(this, Role()) }
            .single()

    fun getByNameOrNull(name : String, inContextName : String? = null) : Role? =
        if (inContextName == null) {
            select { (programmaticName eq name) and contextName.isNull() }
        } else {
            select { (programmaticName eq name) and (contextName eq inContextName) }
        }
            .map { it.toSchematic(this, Role()) }
            .singleOrNull()

    fun getByContext(name : String) : List<Role> =
        select { contextName eq name }
            .map { it.toSchematic(this, Role()) }

}