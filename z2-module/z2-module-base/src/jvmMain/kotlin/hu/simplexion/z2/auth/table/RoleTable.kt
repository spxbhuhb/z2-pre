package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.exposed.SchematicUuidTable

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

}