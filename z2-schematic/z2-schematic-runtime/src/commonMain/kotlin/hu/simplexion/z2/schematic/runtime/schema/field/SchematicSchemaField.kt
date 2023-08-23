package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion
import hu.simplexion.z2.schematic.runtime.placeholder
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

open class SchematicSchemaField<T : Schematic<T>>(
    override var definitionDefault: T?,
) : SchemaField<T> {

    override var name: String = ""
    override var nullable: Boolean = false

    // set by the compiler plugin
    lateinit var companion: SchematicCompanion<T>

    override val type: SchemaFieldType
        get() = SchemaFieldType.Schematic

    override val naturalDefault
        get() = newInstance()

    // TODO create a getCompanion function that may be used to get the companion of the field, so we don't need the lateinit companion
    val schema
        get() = companion.schematicSchema

    // called by the compiler plugin to set the companion
    fun setCompanion(companion: SchematicCompanion<T>) : SchematicSchemaField<T> {
        this.companion = companion
        return this
    }

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

    infix fun default(value: T?): SchematicSchemaField<T> {
        this.definitionDefault = value
        return this
    }

}