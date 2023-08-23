package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.event.Z2Event
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.schematic.runtime.schema.SchemaField

class SchematicEvent(
    override val busHandle: Z2Handle,
    val schematic : Schematic<*>,
    val field : SchemaField<*>
) : Z2Event