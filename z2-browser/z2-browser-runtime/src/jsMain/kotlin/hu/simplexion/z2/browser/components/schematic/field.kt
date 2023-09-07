package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.datepicker.datePicker
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.textfield.outlinedTextField
import hu.simplexion.z2.browser.util.label
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.schematic.runtime.SchematicAccessFunction
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
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

    val field = context.field
    val label = field.label()

    return when (field.type) {
        SchemaFieldType.LocalDate -> localDateField(context, label) as BoundField<T>
        SchemaFieldType.Enum -> enumField(context, label) as BoundField<T>
        else -> textField(context, label)
    }
}

@SchematicAccessFunction
fun <T> Z2.radioField(context: SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor: () -> T): BoundSelectField<T> {
    checkNotNull(context)
    return BoundRadioField(this, context)
}

private fun <T> Z2.textField(context: SchematicAccessContext, label: LocalizedText) =
    BoundTextField<T>(this, context) {
        outlinedTextField("", label, label.support) {
            context.schematic.schematicChange(context.field, it)
        }
    }

private fun Z2.localDateField(context: SchematicAccessContext, label: LocalizedText) =
    BoundLocalDateField(this, context) {
        // FIXME supporting text handling
        datePicker(label = label, supportingText = commonStrings.localDateSupportText) {
            context.schematic.schematicChange(context.field, it)
        }
    }

private fun <T : Enum<T>> Z2.enumField(context: SchematicAccessContext, label: LocalizedText) =
    BoundEnumField(this, context) {
        @Suppress("UNCHECKED_CAST")
        val field = context.field as EnumSchemaField<T>
        val value = field.getValue(context.schematic)
        // FIXME supporting text handling
        radioButtonGroup(value, field.entries.toList()) {
            context.schematic.schematicChange(context.field, it)
        }
    }



