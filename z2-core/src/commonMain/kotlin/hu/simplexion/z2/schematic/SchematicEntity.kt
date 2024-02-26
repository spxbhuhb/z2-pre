package hu.simplexion.z2.schematic

import hu.simplexion.z2.util.UUID

interface SchematicEntity<T> {
    val uuid : UUID<T>
    val name : String
    val schematicFqName : String
}