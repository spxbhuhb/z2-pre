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

    override fun nullable() : NullableSecretSchemaField {
        return NullableSecretSchemaField(definitionDefault, minLength, maxLength, blank)
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
}