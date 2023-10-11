package hu.simplexion.z2.schematic

import hu.simplexion.z2.commons.event.Z2Event
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.validation.SchematicValidationResult

interface SchematicEvent : Z2Event {
    val schematic: Schematic<*>
}

class SchematicFieldEvent(
    override val busHandle: Z2Handle,
    override val schematic: Schematic<*>,
    val field: SchemaField<*>,
    val validationResult: SchematicValidationResult
) : SchematicEvent

class SchematicListFieldEvent(
    override val busHandle: Z2Handle,
    override val schematic: Schematic<*>,
    val field: hu.simplexion.z2.schematic.schema.ListSchemaField<*>,
    val validationResult: SchematicValidationResult
) : SchematicEvent