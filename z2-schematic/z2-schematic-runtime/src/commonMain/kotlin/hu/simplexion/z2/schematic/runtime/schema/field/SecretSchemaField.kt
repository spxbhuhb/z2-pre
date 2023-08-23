package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType

class SecretSchemaField(
    name: String,
    nullable: Boolean,
    definitionDefault: String?,
    minLength: Int?,
    maxLength: Int?,
    blank: Boolean?,
) : StringSchemaField(name, nullable, definitionDefault, minLength, maxLength, blank, null) {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Secret

}