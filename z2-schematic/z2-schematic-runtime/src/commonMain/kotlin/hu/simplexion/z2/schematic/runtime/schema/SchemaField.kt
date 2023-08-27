package hu.simplexion.z2.schematic.runtime.schema

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicChange
import hu.simplexion.z2.schematic.runtime.placeholder
import hu.simplexion.z2.schematic.runtime.schema.validation.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface SchemaField<VT> : ReadWriteProperty<Any, VT> {
    var name: String
    val type: SchemaFieldType
    val isNullable: Boolean
    val definitionDefault: VT?
    val naturalDefault : VT

    /**
     * Called by the compiler plugin to set the field name.
     */
    fun setFieldName(name : String) : SchemaField<VT> {
        this.name = name
        return this
    }

    fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): VT?

    fun validate(anyValue: Any?): FieldValidationResult {
        val fails = mutableListOf<ValidationFailInfo>()
        val value = toTypedValue(anyValue, fails)

        if (value != null) {
            validateValue(value, fails)
        } else {
            validateNull(fails)
        }

        return FieldValidationResult(
            name,
            fails.isEmpty(),
            fails
        )
    }

    suspend fun validateSuspend(anyValue: Any?): FieldValidationResult {
        return validate(anyValue)
    }

    fun validateValue(value: VT, fails: MutableList<ValidationFailInfo>) {

    }

    fun validateNull(fails: MutableList<ValidationFailInfo>) {
        if (!isNullable) fails += fail(validationStrings.nullFail)
    }

    /**
     * Initializes the field to its default value. This does **NOT** go through
     * the normal change process. No change added nor listener called.
     *
     * @throws IllegalStateException  if the value is already initialized
     */
    fun initWithDefault(schematic: Schematic<*>) {
        val value = when {
            definitionDefault != null -> definitionDefault
            isNullable -> null
            else -> naturalDefault
        }
        check(schematic.schematicValues.put(name, value) == null) { "value already initialized" }
    }

    fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder)

    fun decodeProto(schematic: Schematic<*>, fieldNumber : Int, message: ProtoMessage)

    fun getValue(schematic: Schematic<*>) : VT {
        @Suppress("UNCHECKED_CAST") // TODO remove cast from getValue and build on field type
        return schematic.schematicValues[name] as VT
    }

    fun toString(schematic: Schematic<*>) : String {
        return getValue(schematic).toString()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): VT = placeholder()

    override fun setValue(thisRef: Any, property: KProperty<*>, value : VT) = placeholder()

}