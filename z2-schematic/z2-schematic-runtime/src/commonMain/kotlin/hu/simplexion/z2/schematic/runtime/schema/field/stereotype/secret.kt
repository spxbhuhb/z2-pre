package hu.simplexion.z2.schematic.runtime.schema.field.stereotype

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.field.NullableStringSchemaField
import hu.simplexion.z2.schematic.runtime.schema.field.StringSchemaField

open class SecretSchemaField(
    definitionDefault: String?,
    minLength: Int?,
    maxLength: Int?,
    blank: Boolean?,
) : StringSchemaField(definitionDefault, minLength, maxLength, blank, null) {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Secret

    override fun nullable() : NullableSecretSchemaField {
        return NullableSecretSchemaField(definitionDefault, minLength, maxLength, blank)
    }

    override fun toString(schematic: Schematic<*>) : String {
        return "********"
    }

    override fun encodeToString(schematic: Schematic<*>): String {
        return getValue(schematic)
    }
}

open class NullableSecretSchemaField(
    definitionDefault: String?,
    minLength: Int?,
    maxLength: Int?,
    blank: Boolean?,
) : NullableStringSchemaField(definitionDefault, minLength, maxLength, blank, null) {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Secret

    override fun nullable() : NullableSecretSchemaField {
        return this
    }

    override fun toString(schematic: Schematic<*>) : String {
        return "********"
    }

    override fun encodeToString(schematic: Schematic<*>): String {
        return requireNotNull(getValue(schematic))
    }
}