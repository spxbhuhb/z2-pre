package hu.simplexion.z2.schematic.runtime.schema

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.commons.util.PublicApi
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion
import hu.simplexion.z2.schematic.runtime.schema.validation.FieldValidationResult
import hu.simplexion.z2.schematic.runtime.schema.validation.SchematicValidationResult

class Schema<T : Schematic<T>>(
    val companion : SchematicCompanion<T>,
    vararg val fields : SchemaField<T>
) {

    /**
     * Get a field by its name.
     */
    fun getField(fieldName : String) = fields.first { it.name == fieldName }

    /**
     * Calls the `validate` function of all fields in the schema on the value
     * that belongs to the field in [schematic].
     */
    fun validate(schematic : Schematic<*>) : SchematicValidationResult {
        var valid = true
        val fieldResults = mutableMapOf<String,FieldValidationResult>()

        for (field in fields) {
            val fieldResult = field.validate(schematic.schematicValues[field.name])
            valid = valid && fieldResult.valid
            fieldResults[field.name] = fieldResult
        }

        return SchematicValidationResult(
            valid,
            fieldResults
        )
    }

    /**
     * Calls the `validateSuspend` function of all fields in the schema on the value
     * that belongs to the field in [schematic].
     */
    @PublicApi
    suspend fun validateSuspend(schematic : Schematic<*>) : SchematicValidationResult{
        var valid = true
        val fieldResults = mutableMapOf<String,FieldValidationResult>()

        for (field in fields) {
            val fieldResult = field.validateSuspend(schematic.schematicValues[field.name])
            valid = valid && fieldResult.valid
            fieldResults[field.name] = fieldResult
        }

        return SchematicValidationResult(
            valid,
            fieldResults
        )
    }

    /**
     * Initializes all fields to the default value. These changes do **NOT**
     * go into the change list, nor they generate listener calls. Requires
     * `schematicValues` to be empty/
     *
     * @throws  IllegalStateException  `schematicValues` is not empty
     */
    fun initWithDefaults(schematic: Schematic<*>) {
        check(schematic.schematicValues.isEmpty()) { "initWithDefaults called on a non-empty schematic" }
        for (field in fields) {
            field.initWithDefault(schematic)
        }
    }

    @PublicApi
    fun newInstance() : T =
        companion.newInstance()

    fun encodeProto(schematic: Schematic<*>) : ByteArray {
        val builder = ProtoMessageBuilder()
        for (index in fields.indices) {
            fields[index].encodeProto(schematic, index + 1, builder)
        }
        return builder.pack()
    }

    fun decodeProto(schematic: Schematic<*>, message: ProtoMessage?) : Schematic<*> {
        if (message == null) return schematic
        for (index in fields.indices) {
            fields[index].decodeProto(schematic, index + 1, message)
        }
        return schematic
    }

    fun dump(schematic: Schematic<*>, indent : String = "", result : MutableList<String>) : MutableList<String> {
        for (field in fields) {
            result += "${indent}FIELD  name=${field.name}  type=${field.type}  value=${schematic.schematicValues[field.name]}"
        }
        return result
    }
}