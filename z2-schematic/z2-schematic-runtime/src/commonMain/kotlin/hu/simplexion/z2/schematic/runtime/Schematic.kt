package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.nextHandle
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import hu.simplexion.z2.schematic.runtime.schema.Schema
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.field.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration

abstract class Schematic<ST : Schematic<ST>> {

    /**
     * The unique handle of this schematic instance.
     */
    val handle = nextHandle()

    /**
     * The actual values stored in this schematic. Key is the name of the
     * field. Value may be missing if the field is nullable.
     */
    val schematicValues = mutableMapOf<String, Any?>()

    /**
     * Get the schema of this schematic. Returns with the value of
     * `schematicCompanion.schematicSchema`.
     */
    open val schematicSchema: Schema<ST>
        get() = placeholder()

    /**
     * Get the companion object of this schematic.
     */
    open val schematicCompanion: SchematicCompanion<ST>
        get() = placeholder()

    /**
     * Validates the schema (calls [Schema.validate]) and returns with the result.
     */
    val isValid
        get() = schematicSchema.validate(this).valid

    // -----------------------------------------------------------------------------------
    // Change management
    // -----------------------------------------------------------------------------------

    /**
     * Change the value of a field.
     */
    fun schematicChange(field: SchemaField<*>, change: SchematicChange) {
        change.patch(schematicValues)
        EventCentral.fire(SchematicEvent(handle, this, field))
    }

    /**
     * Creates a [SchematicChange] by calling `SchematicField.asChange` and then
     * calls [schematicChange] with it.
     */
    fun schematicChange(fieldIndex: Int, value: Any?) {
        val field = schematicSchema.fields[fieldIndex]
        schematicChange(field, field.asChange(value))
    }

    /**
     * Creates a [SchematicChange] by calling `SchematicField.asChange` and then
     * calls [schematicChange] with it.
     */
    fun schematicChange(field: SchemaField<*>, value: Any?) {
        schematicChange(field, field.asChange(value))
    }

    // -----------------------------------------------------------------------------------
    // Accessor Support
    // -----------------------------------------------------------------------------------

    fun toSchematicAccessContext(fieldName: String): SchematicAccessContext =
        SchematicAccessContext(this, schematicSchema.getField(fieldName), schematicValues[fieldName])

    companion object {

        fun boolean(default: Boolean? = null) = BooleanSchemaField(default)

        fun duration(default: Duration? = null) = DurationSchemaField(default)

        fun email(blank: Boolean? = null) = EmailSchemaField(blank)

        inline fun <reified E : Enum<E>> enum(default: E? = null) = EnumSchemaField(enumValues<E>(), default)

        fun instant(default: Instant? = null) = InstantSchemaField(default)

        fun int(default: Int? = null, min: Int? = null, max: Int? = null) = IntSchemaField(default, min, max)

        fun localDate(default: LocalDate? = null) = LocalDateSchemaField(default)

        fun localDateTime(default: LocalDateTime? = null) = LocalDateTimeSchemaField(default)

        fun long(default: Long? = null, min: Long? = null, max: Long? = null) = LongSchemaField(default, min, max)

        fun <VT : Schematic<VT>> schematic(default: VT? = null) = SchematicSchemaField(default)

        fun phoneNumber(blank: Boolean? = null) = PhoneNumberSchemaField(blank)

        fun secret(
            default: String? = null,
            minLength: Int? = null,
            maxLength: Int? = null,
            blank: Boolean? = null,
        ) = SecretSchemaField(default, minLength, maxLength, blank)

        fun string(
            default: String? = null,
            minLength: Int? = null,
            maxLength: Int? = null,
            blank: Boolean? = null,
            pattern: Regex? = null
        ) = StringSchemaField(default, minLength, maxLength, blank, pattern)

        fun <UT> uuid(default: UUID<UT>? = null, nil: Boolean? = null) = UuidSchemaField(default, nil)

        fun <VT> SchemaField<VT>.nullable(): SchemaField<VT?> {
            this.nullable = true
            @Suppress("UNCHECKED_CAST")
            return this as SchemaField<VT?>
        }
    }
}