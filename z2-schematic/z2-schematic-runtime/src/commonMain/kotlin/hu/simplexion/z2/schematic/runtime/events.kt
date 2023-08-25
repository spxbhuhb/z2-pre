package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.event.Z2Event
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.schematic.runtime.schema.ListSchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.validation.SchematicValidationResult

interface SchematicEvent : Z2Event {
    val schematic: Schematic<*>
    val field: SchemaField<*>
    val validationResult: SchematicValidationResult
}

class SchematicFieldEvent(
    override val busHandle: Z2Handle,
    override val schematic: Schematic<*>,
    override val field: SchemaField<*>,
    override val validationResult: SchematicValidationResult
) : SchematicEvent

class SchematicListFieldEvent(
    override val busHandle: Z2Handle,
    override val schematic: Schematic<*>,
    override val field: ListSchemaField<*>,
    override val validationResult: SchematicValidationResult
) : SchematicEvent