package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType

open class SecretSchemaField(
    definitionDefault: String?,
    minLength: Int?,
    maxLength: Int?,
    blank: Boolean?,
) : StringSchemaField(definitionDefault, minLength, maxLength, blank, null) {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Secret

}