package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.css.displayContents
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.datepicker.DockedDatePicker
import hu.simplexion.z2.browser.util.io
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import kotlinx.browser.document
import kotlinx.datetime.LocalDate
import org.w3c.dom.HTMLElement

class BoundLocalDateField(
    parent: Z2? = null,
    context: SchematicAccessContext,
    buildFun: Z2.() -> DockedDatePicker
) : BoundField<LocalDate>, Z2(
    parent,
    document.createElement("div") as HTMLElement,
    arrayOf(displayContents)
) {
    override val schematic = context.schematic

    @Suppress("UNCHECKED_CAST")
    override val field = context.field as SchemaField<LocalDate>

    override var fullSuspendValidation : FullSuspendValidation<LocalDate>? = null

    lateinit var datePicker : DockedDatePicker

    override var readOnly: Boolean = false
        set(value) {
            field = value
            datePicker.readOnly = value
        }

    init {
        attach(schematic) {

            val value = field.getValue(schematic)
            datePicker.value = value

            if (readOnly) return@attach

            val schemaResult = it.validationResult.fieldResults[field.name]
            val valid = schemaResult?.valid ?: true

            if (fullSuspendValidation == null || !valid) {
                datePicker.setState(!valid, schemaResult?.fails?.firstOrNull()?.message)
                return@attach
            }

            fullSuspendValidation?.let { validation ->
                io {
                    val result = validation(schematic, field, value)
                    // checking for the value so if the user changed it since we won't set it to an old check result
                    if (value == datePicker.value) datePicker.setState(!result.valid, result.fails.firstOrNull()?.message)
                }
            }
        }

        datePicker = buildFun()
        datePicker.value = field.getValue(schematic)
    }

}