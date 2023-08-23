package hu.simplexion.z2.schematic.runtime.access

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField

class SchematicAccessContext(
    val schematic : Schematic<*>,
    val field : SchemaField<*>,
    val value : Any?
)
