package hu.simplexion.z2.schematic.runtime.schema

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicChange
import hu.simplexion.z2.schematic.runtime.schema.validation.FieldValidationResult
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfoNull

interface SchemaField<T> {
    val name: String
    val type: SchemaFieldType
    val nullable: Boolean
    val definitionDefault: T?
    val naturalDefault : T

    fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): T?

    fun validate(anyValue: Any?): FieldValidationResult {
        val fails = mutableListOf<ValidationFailInfo>()
        val value = toTypedValue(anyValue, fails)

        validateNullable(value, fails)

        return FieldValidationResult(
            name,
            fails.isEmpty(),
            fails
        )
    }

    suspend fun validateSuspend(anyValue: Any?): FieldValidationResult {
        return validate(anyValue)
    }

    fun validateNullable(value: T?, fails: MutableList<ValidationFailInfo>) {
        when {
            value != null -> validateNotNullable(value, fails)
            !nullable -> fails += ValidationFailInfoNull
        }
    }

    fun validateNotNullable(value: T, fails: MutableList<ValidationFailInfo>)

    fun asChange(value: Any?) = SchematicChange(name, value)

    /**
     * Initializes the field to its default value. This does **NOT** go through
     * the normal change process. No change added nor listener called.
     *
     * @throws IllegalStateException  if the value is already initialized
     */
    fun initWithDefault(schematic: Schematic<*>) {
        val value = when {
            definitionDefault != null -> definitionDefault
            nullable -> null
            else -> naturalDefault
        }
        check(schematic.schematicValues.put(name, value) == null) { "value already initialized" }
    }

    fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder)

    fun decodeProto(schematic: Schematic<*>, fieldNumber : Int, message: ProtoMessage)

}