package hu.simplexion.z2.schematic.access

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField

class SchematicAccessContext(
    val schematic : Schematic<*>,
    val field : SchemaField<*>,
    val value : Any?
)
