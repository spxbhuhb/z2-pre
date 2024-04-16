package hu.simplexion.z2.schematic

import hu.simplexion.z2.adaptive.binding.AdaptivePropertyProvider
import hu.simplexion.z2.adaptive.binding.AdaptiveStateVariableBinding
import hu.simplexion.z2.deprecated.event.EventCentral
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
import hu.simplexion.z2.util.Z2Handle
import hu.simplexion.z2.util.placeholder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

abstract class Schematic<ST : Schematic<ST>> : SchematicNode, LocalizationProvider, AdaptivePropertyProvider {

    var schematicStateOrNull: SchematicState? = null

    /**
     * The state of this schematic instance. The state is created on-demand, typically
     * when a listener is attached to the schematic.
     */
    override val schematicState: SchematicState
        get() = schematicStateOrNull ?: SchematicState(this).also { schematicStateOrNull = it }

    val schematicHandle: Z2Handle
        get() = schematicState.handle

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
        // this is necessary because of generic types
        // if replaced `schematicCompanion.schematicSchema` the compiler throws an error
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
     * If there are any listeners (that is, [schematicListenerCount] is not 0), validates
     * the schematic and fires a [SchematicFieldEvent].
     */
    fun schematicChange(field: SchemaField<*>, value: Any?) {
        val fails = mutableListOf<ValidationFailInfo>()
        val typedValue = field.toTypedValue(value, fails)

        check(fails.isEmpty()) { "cannot change field value: ${this::class.simpleName}.${field.name} value is type of ${value?.let { it::class.simpleName }}" }

        if (typedValue is SchematicNode) {
            // TODO think about setting the schematic parent, is it a problem if there is already a parent?
            typedValue.schematicState.parent = this
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
        if (schematicState.listenerCount <= 0) return
        val validationResult = schematicSchema.validate(this)
        EventCentral.fire(SchematicFieldEvent(schematicState.handle, this, field, validationResult))
    }

    // -----------------------------------------------------------------------------------
    // Adaptive Support
    // -----------------------------------------------------------------------------------

    override fun getValue(path: Array<String>): Any? {
        require(path.size == 1) { "deep paths are not supported yet" }
        return schematicValues[path[0]]
    }

    override fun setValue(path: Array<String>, value: Any?) {
        require(path.size == 1) { "deep paths are not supported yet" }
        schematicValues[path[0]] = value
    }

    override fun addBinding(binding: AdaptiveStateVariableBinding<*>) {
        // FIXME bindings += binding
    }

    override fun removeBinding(binding: AdaptiveStateVariableBinding<*>) {
        // FIXME bindings += binding
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

        fun localTime(default: LocalTime? = null, min: LocalTime? = null, max: LocalTime? = null) = LocalTimeSchemaField(default, min, max)

        fun localDateTime(default: LocalDateTime? = null) = LocalDateTimeSchemaField(default)

        fun long(default: Long? = null, min: Long? = null, max: Long? = null) = LongSchemaField(default, min, max)

        fun <VT : Schematic<VT>> schematic(default: VT? = null) = SchematicSchemaField(default)

        fun <VT : Schematic<VT>> schematicList(default: MutableList<VT>? = null) = SchematicListSchemaField(default)

        fun phoneNumber(blank: Boolean? = null) = PhoneNumberSchemaField(blank)

        fun <RT : SchematicEntity<RT>> reference(
            validForCreate: Boolean = false,
            default: UUID<RT>? = null,
            nil: Boolean? = null
        ) = ReferenceSchemaField(default, nil, validForCreate)

        fun <RT : SchematicEntity<RT>> referenceList(
            default: MutableList<UUID<RT>>? = null,
            nil: Boolean? = null
        ) = ReferenceListSchemaField(default, nil)

        fun <ST : SchematicEntity<ST>> self() = UuidSchemaField<ST>(null, null, true)

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

        fun <GT> generic() = GenericSchemaField<GT>()

        fun <LT> list() = ListSchemaField<LT, GenericSchemaField<LT>>(GenericSchemaField())
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