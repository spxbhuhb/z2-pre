package hu.simplexion.z2.schematic

import hu.simplexion.z2.adaptive.event.EventCentral
import hu.simplexion.z2.localization.LocalizationProvider
import hu.simplexion.z2.localization.NonLocalized
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.schema.Schema
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.field.*
import hu.simplexion.z2.schematic.schema.field.stereotype.EmailSchemaField
import hu.simplexion.z2.schematic.schema.field.stereotype.PhoneNumberSchemaField
import hu.simplexion.z2.schematic.schema.field.stereotype.SecretSchemaField
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.nextHandle
import hu.simplexion.z2.util.placeholder
import hu.simplexion.z2.util.stringPlaceholder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

abstract class Schematic<ST : Schematic<ST>> : SchematicNode, LocalizationProvider {

    @NonLocalized
    override var schematicParent: SchematicNode? = null

    @NonLocalized
    override val schematicHandle = nextHandle()

    @NonLocalized
    override var schematicListenerCount = 0

    /**
     * The actual values stored in this schematic. Key is the name of the
     * field. Value may be missing if the field is nullable.
     */
    @NonLocalized
    val schematicValues = mutableMapOf<String, Any?>()

    /**
     * Get the schema of this schematic. Returns with the value of
     * `schematicCompanion.schematicSchema`.
     */
    @NonLocalized
    open val schematicSchema: Schema<ST>
        get() = placeholder()

    /**
     * The fully qualified name of this schematic.
     */
    @NonLocalized
    open val schematicFqName: String
        get() = schematicCompanion.schematicFqName

    /**
     * Get the companion object of this schematic.
     */
    @NonLocalized
    open val schematicCompanion: SchematicCompanion<ST>
        get() = placeholder()

    /**
     * Validates the schema (calls [Schema.validate] with `create = false`) and
     * returns with the result. For any schematics that are about to be created
     * use [isValidForCreate] as [isValid] usually fails for them (because of
     * field values that are not finalized yet, such as ids).
     */
    @NonLocalized
    val isValid
        get() = schematicSchema.validate(this).valid

    /**
     * Validates the schema (calls [Schema.validate] with `create = true`) and
     * returns with the result.
     */
    @NonLocalized
    val isValidForCreate
        get() = schematicSchema.validate(this).validForCreate

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

        fun decimal(scale: Int = 2, precision: Int = 18, default: Long? = null, min: Long? = null, max: Long? = null) =
            DecimalSchemaField(scale, precision, default, min, max)

        fun duration(default: Duration? = null) = DurationSchemaField(default)

        fun email(blank: Boolean? = null) = EmailSchemaField(blank)

        inline fun <reified E : Enum<E>> enum(default: E? = null) = EnumSchemaField(enumValues<E>(), default)

        fun instant(default: Instant? = null) = InstantSchemaField(default)

        fun int(default: Int? = null, min: Int? = null, max: Int? = null) = IntSchemaField(default, min, max)

        fun localDate(default: LocalDate? = null) = LocalDateSchemaField(default)

        fun localTime(default: LocalTime? = null) = LocalTimeSchemaField(default)

        fun localDateTime(default: LocalDateTime? = null) = LocalDateTimeSchemaField(default)

        fun long(default: Long? = null, min: Long? = null, max: Long? = null) = LongSchemaField(default, min, max)

        fun <VT : Schematic<VT>> schematic(default: VT? = null) = SchematicSchemaField(default)

        fun <VT : Schematic<VT>> schematicList(default: MutableList<VT>? = null) = SchematicListSchemaField(default)

        fun phoneNumber(blank: Boolean? = null) = PhoneNumberSchemaField(blank)

        fun <RT> reference(
            validForCreate: Boolean = false,
            default: UUID<RT>? = null,
            nil: Boolean? = null,
            entityFqName : String = stringPlaceholder
        ) = ReferenceSchemaField(default, nil, validForCreate, entityFqName)

        fun <RT> referenceList(
            default: MutableList<UUID<RT>>? = null,
            nil: Boolean? = null
        ) = ReferenceListSchemaField(default, nil, stringPlaceholder)

        @Suppress("UnusedReceiverParameter") // it is used by the syntax checker
        fun <ST : SchematicEntity<ST>> ST.self() = ReferenceSchemaField<ST>(null, null, true, stringPlaceholder)

        fun secret(
            default: String? = null,
            minLength: Int? = null,
            maxLength: Int? = null,
            blank: Boolean? = null,
        ) = SecretSchemaField(default, minLength, maxLength, blank)

        fun string(
            maxLength: Int? = null,
            default: String? = null,
            minLength: Int? = null,
            blank: Boolean? = null,
            pattern: Regex? = null
        ) = StringSchemaField(default, minLength, maxLength, blank, pattern)

        fun stringList(
            maxLength: Int? = null,
            default: MutableList<String>? = null,
            minLength: Int? = null,
            blank: Boolean? = null,
            pattern: Regex? = null
        ) = StringListSchemaField(default, minLength, maxLength, blank, pattern)

        fun <UT> uuid(
            validForCreate: Boolean = false,
            default: UUID<UT>? = null,
            nil: Boolean? = null,
        ) = UuidSchemaField(default, nil, validForCreate)

        fun <UT> uuidList(
            default: MutableList<UUID<UT>>? = null,
            nil: Boolean? = null
        ) = UuidListSchemaField(default, nil)

    }

    // -----------------------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------------------

    fun schematicSet(fieldName: String, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        (schematicSchema.fields.first { it.name == fieldName } as SchemaField<Any>).setValue(this, value)
    }

    fun schematicGet(fieldName: String): Any? {
        return schematicSchema.fields.first { it.name == fieldName }.getValue(this)
    }

    // FIXME this is a real mess, fix schematic field type handling already!
    fun copy(): ST {
        val newInstance = schematicCompanion.newInstance()
        for (field in schematicSchema.fields) {
            newInstance.schematicValues[field.name] = field.copy(this.schematicValues[field.name])
        }
        return newInstance
    }

    fun copyFrom(source: ST) {
        for (field in schematicSchema.fields) {
            field.copy(source, this)
        }
    }

    fun reset() {
        for (field in schematicSchema.fields) {
            field.reset(this)
        }
    }

    fun validate(forCreate: Boolean = false) =
        schematicSchema.validate(this).also {
            if (forCreate) it.validForCreate else it.valid
        }

    override fun toString(): String {
        return this::class.simpleName + "(" + toString("=", ",") + ")"
    }

    fun toString(valueSeparator: String = "=", fieldSeparator: String = ", "): String {
        val fields = mutableListOf<String>()
        for (field in schematicSchema.fields) {
            fields += field.name + valueSeparator + field.toString(this)
        }
        return fields.joinToString(fieldSeparator)
    }
}