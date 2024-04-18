package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.schematic.SchematicList
import hu.simplexion.z2.schematic.schema.Schema
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder

interface SchematicSchemaFieldCommon<T : Schematic<T>> {

    val schema : Schema<T>
        get() = companion.schematicSchema

    var companion: SchematicCompanion<T>

    @Suppress("UNCHECKED_CAST")
    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): T? {
        if (anyValue == null) {
            fails += fail(validationStrings.nullFail)
            return null
        }

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

    fun validateValueCommon(value: T, fails: MutableList<ValidationFailInfo>) {
        if (! schema.validate(value).valid) {
            fails += fail(validationStrings.schematicFail)
        }
    }
}

open class SchematicSchemaField<T : Schematic<T>>(
    override var definitionDefault: T?,
) : SchemaField<T>, SchematicSchemaFieldCommon<T> {

    override val type: SchemaFieldType get() = SchemaFieldType.Schematic
    override val isNullable: Boolean = false
    override val naturalDefault get() = newInstance()

    override var name: String = ""

    // TODO create a getCompanion function that may be used to get the companion of the field, so we don't need the lateinit companion

    // set by the compiler plugin
    override lateinit var companion: SchematicCompanion<T>

    // called by the compiler plugin to set the companion
    fun setCompanion(companion: SchematicCompanion<T>) : SchematicSchemaField<T> {
        this.companion = companion
        return this
    }

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): T? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: T, fails: MutableList<ValidationFailInfo>) {
        validateValueCommon(value, fails)
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

    fun nullable() : NullableSchematicSchemaField<T> {
        return NullableSchematicSchemaField(definitionDefault)
    }

    override fun copy(value: Any?): T? {
        if (value == null) return null
        @Suppress("UNCHECKED_CAST")
        value as Schematic<T>
        return value.copy()
    }

}

open class NullableSchematicSchemaField<T : Schematic<T>>(
    override var definitionDefault: T?,
) : SchemaField<T?>, SchematicSchemaFieldCommon<T> {

    override val type: SchemaFieldType get() = SchemaFieldType.Schematic
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    // TODO create a getCompanion function that may be used to get the companion of the field, so we don't need the lateinit companion

    // set by the compiler plugin
    override lateinit var companion: SchematicCompanion<T>

    // called by the compiler plugin to set the companion
    fun setCompanion(companion: SchematicCompanion<T>) : NullableSchematicSchemaField<T> {
        this.companion = companion
        return this
    }

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): T? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: T?, fails: MutableList<ValidationFailInfo>) {
        if (value == null) return
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.instanceOrNull(fieldNumber, fieldNumber + 1, schema.companion, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceOrNull(fieldNumber, fieldNumber + 1, schema.companion)
        schematic.schematicValues[name] = value
    }

    fun newInstance(): T = companion.newInstance()

    infix fun default(value: T?): NullableSchematicSchemaField<T> {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableSchematicSchemaField<T> {
        return this
    }

}

class SchematicListSchemaField<T : Schematic<T>>(
    definitionDefault : MutableList<T>?
) : ListSchemaField<T, SchematicSchemaField<T>>(
    SchematicSchemaField(null)
) {

    override var name: String = ""

    override val definitionDefault = definitionDefault?.let { SchematicList(definitionDefault, this) }

    // called by the compiler plugin to set the companion
    fun setCompanion(companion: SchematicCompanion<T>) : SchematicListSchemaField<T> {
        itemSchemaField.companion = companion
        return this
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instanceList(fieldNumber, itemSchemaField.companion, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceList(fieldNumber, itemSchemaField.companion)
        schematic.schematicValues[name] = SchematicList(value, this).also { it.schematicState.parent = schematic }
    }
}