package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

class SchematicSchemaField<T : Schematic<T>>(
    override val name: String,
    override val nullable: Boolean,
    override val definitionDefault: T?,
    val companion: SchematicCompanion<T>
) : SchemaField<T> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Schematic

    override val naturalDefault
        get() = newInstance()

    val schema = companion.schematicSchema

    @Suppress("UNCHECKED_CAST")
    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): T? {
        if (anyValue == null) return null

        if (anyValue !is Schematic<*>) {
            fails += fail(validationStrings.schematicFail)
            return null
        }

        if (anyValue.schematicSchema !== schema) {
            fails += fail(validationStrings.schematicFail)
            return null
        }

        return anyValue as T
    }

    override fun validateNotNullable(value: T, fails: MutableList<ValidationFailInfo>) {
        if (! schema.validate(value).valid) {
            fails += fail(validationStrings.schematicFail)
        }
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, schema.companion, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, schema.companion)
        schematic.schematicValues[name] = value
    }

    fun newInstance(): T = companion.newInstance()

}