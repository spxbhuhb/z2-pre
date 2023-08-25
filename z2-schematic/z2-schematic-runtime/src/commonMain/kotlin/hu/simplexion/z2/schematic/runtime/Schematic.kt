package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.nextHandle
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import hu.simplexion.z2.schematic.runtime.schema.Schema
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.field.*
import hu.simplexion.z2.schematic.runtime.schema.field.stereotype.EmailSchemaField
import hu.simplexion.z2.schematic.runtime.schema.field.stereotype.PhoneNumberSchemaField
import hu.simplexion.z2.schematic.runtime.schema.field.stereotype.SecretSchemaField
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

abstract class Schematic<ST : Schematic<ST>> : SchematicNode {

    override var schematicParent: SchematicNode? = null

    override val schematicHandle = nextHandle()

    override var schematicListenerCount = 0

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
     * Changes the value of a field if it can be converted to the given data type.
     * Throws exception if the conversion fails.
     * If there are any listeners (that is, [listenerCount] is not 0), validates
     * the schematic and fires a [SchematicFieldEvent].
     */
    fun schematicChange(field: SchemaField<*>, value: Any?) {
        val fails = mutableListOf<ValidationFailInfo>()
        val typedValue = field.toTypedValue(value, fails)

        check(fails.isEmpty()) { "cannot change field value: ${this::class.simpleName}.${field.name} value is type of ${value?.let { it::class.simpleName }}" }

        if (typedValue is SchematicNode) {
            typedValue.schematicParent = this
        }

        schematicValues[field.name] = typedValue

        fireEvent(field)
    }

    /**
     * Changes the value of a field specified by [fieldIndex].
     */
    fun schematicChange(fieldIndex: Int, value: Any?) {
        schematicChange(schematicSchema.fields[fieldIndex], value)
    }

    override fun fireEvent(field: SchemaField<*>) {
        if (schematicListenerCount <= 0) return
        val validationResult = schematicSchema.validate(this)
        EventCentral.fire(SchematicFieldEvent(schematicHandle, this, field, validationResult))
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

        fun <VT : Schematic<VT>> schematicList(default: MutableList<VT>? = null) = SchematicListSchemaField(default)

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

        fun stringList(
            default: MutableList<String>? = null,
            minLength: Int? = null,
            maxLength: Int? = null,
            blank: Boolean? = null,
            pattern: Regex? = null
        ) = StringListSchemaField(default, minLength, maxLength, blank, pattern)

        fun <UT> uuid(
            default: UUID<UT>? = null,
            nil: Boolean? = null
        ) = UuidSchemaField(default, nil)

        fun <UT> uuidList(
            default: MutableList<UUID<UT>>? = null,
            nil: Boolean? = null
        ) = UuidListSchemaField(default, nil)

    }
}