package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.util.io
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import hu.simplexion.z2.schematic.runtime.schema.SchemaField

@Suppress("UNCHECKED_CAST")
open class BoundField<T>(
    parent: Z2,
    context: SchematicAccessContext,
    buildFun: BoundField<T>.() -> ValueField<T>,
) : Z2(parent) {

    val schematic = context.schematic
    val schemaField = context.field

    var fullSuspendValidation: FullSuspendValidation<T>? = null

    val uiField = buildFun()

    init {
        attachListener(schematic) {

            val value = schemaField.getValue(schematic) as T
            uiField.value = value

            if (uiField.state.readOnly) return@attachListener

            val schemaResult = it.validationResult.fieldResults[schemaField.name]
            val valid = schemaResult?.valid ?: true

            if (fullSuspendValidation == null || !valid) {
                uiField.state.error = !valid
                uiField.state.errorText = schemaResult?.fails?.firstOrNull()?.message
                return@attachListener
            }

            fullSuspendValidation?.let { validation ->
                io {
                    val result = validation(schematic, schemaField as SchemaField<T>, value)
                    // checking for the value so if the user changed it since we won't set it to an old check result
                    if (value == uiField.value) {
                        uiField.state.error = !result.valid
                        uiField.state.errorText = result.fails.firstOrNull()?.message
                    }
                }
            }
        }

        uiField.value = schemaField.getValue(schematic) as T
    }

    infix fun validateSuspend(validation: SuspendValidation<T>) {
        fullSuspendValidation = { _, _, value -> validation(value) }
    }

    infix fun validateSuspendFull(validation: FullSuspendValidation<T>) {
        fullSuspendValidation = validation
    }

    infix fun readOnly(readOnly: Boolean) {
        uiField.state.readOnly = readOnly
    }

    infix fun disabled(disabled: Boolean) {
        uiField.state.disabled = disabled
    }

}