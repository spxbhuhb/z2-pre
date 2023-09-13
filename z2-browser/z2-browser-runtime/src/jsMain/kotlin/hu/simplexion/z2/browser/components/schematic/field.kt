package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.stereotypes.EmailField
import hu.simplexion.z2.browser.field.stereotypes.IntField
import hu.simplexion.z2.browser.field.stereotypes.LongField
import hu.simplexion.z2.browser.field.stereotypes.PhoneNumberField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.datepicker.datePicker
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.browser.material.textfield.FieldConfig.Companion.defaultFieldStyle
import hu.simplexion.z2.browser.material.textfield.textField
import hu.simplexion.z2.browser.util.label
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.schematic.runtime.SchematicAccessFunction
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import hu.simplexion.z2.schematic.runtime.dump
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.field.EnumSchemaField

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
        val label = field.label()

        when (field.type) {
            SchemaFieldType.Email -> emailField(context, label) as BoundField<T>
            SchemaFieldType.Enum -> enumField(context, label) as BoundField<T>
            SchemaFieldType.Int -> intField(context, label) as BoundField<T>
            SchemaFieldType.LocalDate -> localDateField(context, label) as BoundField<T>
            SchemaFieldType.Long -> longField(context, label) as BoundField<T>
            SchemaFieldType.Phone -> phoneNumberField(context, label) as BoundField<T>
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

private fun Z2.emailField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        EmailField(
            this,
            context.field.getValue(context.schematic) as String,
            FieldState(label),
            FieldConfig { context.schematic.schematicChange(context.field, it.value) }
        )
    }

private fun <T : Enum<T>> Z2.enumField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {

        @Suppress("UNCHECKED_CAST")
        val field = context.field as EnumSchemaField<T>
        val value = field.getValue(context.schematic)

        // FIXME supporting text handling
        radioButtonGroup(value, field.entries.toList()) {
            context.schematic.schematicChange(context.field, it)
        }
    }

private fun Z2.intField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        IntField(
            this,
            context.field.getValue(context.schematic) as Int,
            FieldState(label),
            FieldConfig { context.schematic.schematicChange(context.field, it.value) }
        )
    }

private fun Z2.longField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        LongField(
            this,
            context.field.getValue(context.schematic) as Long,
            FieldState(label),
            FieldConfig { context.schematic.schematicChange(context.field, it.value) }
        )
    }

private fun Z2.localDateField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        datePicker(label = label, supportText = commonStrings.localDateSupportText) {
            context.schematic.schematicChange(context.field, it)
        }
    }

private fun Z2.phoneNumberField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        PhoneNumberField(
            this,
            context.field.getValue(context.schematic) as String,
            FieldState(label),
            FieldConfig { context.schematic.schematicChange(context.field, it.value) }
        )
    }

private fun Z2.stringField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        val field = context.field
        val value = field.getValue(context.schematic) as String

        textField(value, defaultFieldStyle, label, label.support) {
            context.schematic.schematicChange(field, it.value)
        }
    }