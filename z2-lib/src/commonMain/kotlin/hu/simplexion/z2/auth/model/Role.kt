package hu.simplexion.z2.auth.model

import hu.simplexion.z2.localization.localizedTextStore
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicCompanion

class Role : Schematic<Role>() {

    var uuid by uuid<Role>(true)

    var contextName by string(maxLength = 50).nullable()
    var programmaticName by string(maxLength = 100)
    var displayName by string(maxLength = 50)
    var group by boolean()
    var displayOrder by int().nullable()

    companion object : SchematicCompanion<Role>

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Role) return false
        return uuid == other.uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    override fun toString() : String =
        localizedTextStore[uuid.toString()]?.value ?: displayName
}