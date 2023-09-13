package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.datepicker.datePicker
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.textfield.TextFieldConfig.Companion.defaultFieldStyle
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
            SchemaFieldType.LocalDate -> localDateField(context, label) as BoundField<T>
            SchemaFieldType.Enum -> enumField(context, label) as BoundField<T>
            else -> boundTextField(context, label) as BoundField<T>
        }
    } catch (ex : Exception) {
        println("error in field builder")
        println("schematic: ${context.schematic.dump()}")
        println("field: ${context.field.name}")
        throw ex
    }
}

private fun Z2.boundTextField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        textField("", defaultFieldStyle, label, label.support) {
            context.schematic.schematicChange(context.field, it)
        }
    }

private fun Z2.localDateField(context: SchematicAccessContext, label: LocalizedText) =
    BoundField(this, context) {
        datePicker(label = label, supportText = commonStrings.localDateSupportText) {
            context.schematic.schematicChange(context.field, it)
        }
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



