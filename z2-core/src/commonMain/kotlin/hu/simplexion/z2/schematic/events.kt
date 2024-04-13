package hu.simplexion.z2.schematic

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.field.ListSchemaField
import hu.simplexion.z2.schematic.schema.validation.SchematicValidationResult
import hu.simplexion.z2.util.Z2Handle

interface SchematicEvent : Z2Event {
    val schematic: Schematic<*>
}

open class SchematicFieldEvent(
    override val busHandle: Z2Handle,
    override val schematic: Schematic<*>,
    open val field: SchemaField<*>,
    val validationResult: SchematicValidationResult
) : SchematicEvent

class SchematicListFieldEvent(
    busHandle: Z2Handle,
    schematic: Schematic<*>,
    override val field: ListSchemaField<*, *>,
    validationResult: SchematicValidationResult
) : SchematicFieldEvent(busHandle, schematic, field, validationResult)

class SchematicValidation(
    override val busHandle: Z2Handle,
    override val schematic: Schematic<*>,
    val validationResult: SchematicValidationResult
) : SchematicEvent