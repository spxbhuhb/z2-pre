package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.stereotype.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.datetimepicker.dateTimePicker
import hu.simplexion.z2.browser.material.datepicker.datePicker
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.switch.SwitchConfig
import hu.simplexion.z2.browser.material.switch.SwitchField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.browser.material.textfield.FieldConfig.Companion.defaultFieldStyle
import hu.simplexion.z2.browser.material.textfield.textField
import hu.simplexion.z2.browser.material.timepicker.timePicker
import hu.simplexion.z2.localization.label
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.localized
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.schematic.SchematicAccessFunction
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.dump
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.field.DecimalSchemaFieldDefault
import hu.simplexion.z2.schematic.schema.field.EnumSchemaField
import hu.simplexion.z2.schematic.schema.field.EnumSchemaFieldCommon
import hu.simplexion.z2.schematic.schema.field.NullableEnumSchemaField
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * An input for the schematic field accessed by [accessor]. The actual input depends on
 * the type of the field.
 */
@Suppress("UNCHECKED_CAST")
@SchematicAccessFunction
fun <T> Z2.field(context: SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor: () -> T): BoundField<T> {
    checkNotNull(context)

    return try {
        val field = context.field
        val label = field.label(context.schematic)

        when (field.type) {
            SchemaFieldType.Boolean -> booleanField(context, label) as BoundField<T>
            SchemaFieldType.Decimal -> decimalField(context, label) as BoundField<T>
            SchemaFieldType.Email -> emailField(context, label) as BoundField<T>
            SchemaFieldType.Enum -> enumField(context, label) as BoundField<T>
            SchemaFieldType.Instant -> instantField(context, label) as BoundField<T>
            SchemaFieldType.Int -> intField(context, label) as BoundField<T>
            SchemaFieldType.LocalDate -> localDateField(context, label) as BoundField<T>
            SchemaFieldType.LocalDateTime -> localDateTimeField(context, label) as BoundField<T>
            SchemaFieldType.LocalTime -> localTimeField(context, label) as BoundField<T>
            SchemaFieldType.Long -> longField(context, label) as BoundField<T>
            SchemaFieldType.PhoneNumber -> phoneNumberField(context, label) as BoundField<T>
            SchemaFieldType.Secret -> secretField(context, label) as BoundField<T>
            SchemaFieldType.String -> stringField(context, label) as BoundField<T>
            else -> throw NotImplementedError("field type ${field.type} is not implemented yet")
        }
    } catch (ex: Exception) {
        println("error in field builder")
        println("schematic: ${context.schematic.dump()}")
        println("field: ${context.field.name}")
        throw ex
    }
}

private fun Z2.booleanField(context: SchematicAccessContext, label: LocalizedText): BoundField<Boolean> =
    BoundField(this, context) {
        SwitchField(
            this,
            FieldState().also {
                it.label = label.localeCapitalized
            },
            SwitchConfig().also {
                it.addLabel = true
                it.onChange = { f -> context.schematic.schematicChange(context.field, f.value) }
            }
        ).main().also {
            it.valueOrNull = context.field.getValue(context.schematic) as? Boolean?
        }
    }

private fun Z2.emailField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        EmailField(
            this,
            FieldState(label),
            FieldConfig(
                decodeFromString = { it }
            ) { context.schematic.schematicChange(context.field, it.value) }
        ).main().also {
            it.valueOrNull = context.field.getValue(context.schematic) as? String
        }
    }

@Suppress("UNCHECKED_CAST")
private fun <T : Enum<T>> Z2.enumField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {

        val field = context.field

        val value = if (field.isNullable) {
            field as NullableEnumSchemaField<T>
            field.getValue(context.schematic)
        } else {
            field as EnumSchemaField<T>
            field.getValue(context.schematic)
        }

        field as EnumSchemaFieldCommon<T>

        // FIXME supporting text handling
        radioButtonGroup(value, field.entries.toList(), label = label, style = defaultFieldStyle, { + it.localized }) {
            context.schematic.schematicChange(context.field, it)
        }
    }

private fun Z2.decimalField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        DecimalField(
            this,
            FieldState(label),
            FieldConfig(
                decodeFromString = { throw IllegalStateException("decimal field should override decodeFromString") }
            ) { it.valueOrNull?.let { context.schematic.schematicChange(context.field, it) } },
            (context.field as DecimalSchemaFieldDefault).scale
        ).main().also {
            it.valueOrNull = context.field.getValue(context.schematic) as Long?
        }
    }

private fun Z2.instantField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        InstantField(
            this,
            FieldState(label),
            FieldConfig(
                decodeFromString = { null }
            ) { it.valueOrNull?.let { context.schematic.schematicChange(context.field, it) } }
        ).main().also {
            it.valueOrNull = context.field.getValue(context.schematic) as Instant?
        }
    }

private fun Z2.intField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        IntField(
            this,
            FieldState(label),
            FieldConfig(
                decodeFromString = { it.toIntOrNull() }
            ) { context.schematic.schematicChange(context.field, it.value) }
        ).main().also {
            it.value = context.field.getValue(context.schematic) as Int
        }
    }

private fun Z2.longField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        LongField(
            this,
            FieldState(label),
            FieldConfig(
                decodeFromString = { it.toLongOrNull() }
            ) { it.valueOrNull?.let { context.schematic.schematicChange(context.field, it) } }
        ).main().also {
            it.valueOrNull = context.field.getValue(context.schematic) as Long?
        }
    }

private fun Z2.localDateField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        datePicker(label = label) {
            context.schematic.schematicChange(context.field, it)
        }.main().also {
            it.valueOrNull = context.field.getValue(context.schematic) as LocalDate?
        }
    }

private fun Z2.localDateTimeField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        dateTimePicker(label = label) {
            context.schematic.schematicChange(context.field, it)
        }.main().also {
            it.valueOrNull = context.field.getValue(context.schematic) as LocalDateTime?
        }
    }

private fun Z2.localTimeField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        timePicker(label = label) {
            context.schematic.schematicChange(context.field, it)
        }.main().also {
            context.field.getValue(context.schematic) as LocalTime?
        }
    }

private fun Z2.phoneNumberField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        PhoneNumberField(
            this,
            FieldState(label),
            FieldConfig(
                decodeFromString = { it }
            ) { context.schematic.schematicChange(context.field, it.value) }
        ).main().also {
            it.value = context.field.getValue(context.schematic) as String
        }
    }

private fun Z2.secretField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        SecretField(
            this,
            FieldState(label),
            FieldConfig(
                decodeFromString = { it }
            ) { context.schematic.schematicChange(context.field, it.value) }
        ).main().also {
            it.value = context.field.getValue(context.schematic) as String
        }
    }

private fun Z2.stringField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        val field = context.field

        textField(context.value as? String ?: "", defaultFieldStyle, label, label.support) {
            context.schematic.schematicChange(field, it.value)
        }
    }