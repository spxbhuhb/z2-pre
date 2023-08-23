package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

open class IntSchemaField(
    override var definitionDefault: Int?,
    var min: Int?,
    var max: Int?
) : SchemaField<Int> {

    override var name: String = ""
    override var nullable: Boolean = false

    override val type: SchemaFieldType
        get() = SchemaFieldType.Int

    override val naturalDefault = 0

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Int? {
        if (anyValue == null) return null

        return when (anyValue) {
            is Int -> anyValue
            is Number -> anyValue.toInt()
            is String -> anyValue.toIntOrNull()
            else -> {
                fails += fail(validationStrings.integerFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: Int, fails: MutableList<ValidationFailInfo>) {
        min?.let { if (value < it) fails += fail(validationStrings.minValueFail, it) }
        max?.let { if (value > it) fails += fail(validationStrings.maxValueFail, it) }
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.int(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.int(fieldNumber)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Int): IntSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Int): IntSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Int): IntSchemaField {
        this.max = value
        return this
    }

}