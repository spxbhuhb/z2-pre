package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.css.displayContents
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.radiobutton.RadioButtonGroup
import hu.simplexion.z2.browser.util.io
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

class BoundRadioField<T>(
    parent: Z2? = null,
    context: SchematicAccessContext
) : BoundSelectField<T>, Z2(
    parent,
    document.createElement("div") as HTMLElement,
    arrayOf(displayContents)
) {
    override val schematic = context.schematic

    @Suppress("UNCHECKED_CAST")
    override val field = context.field as SchemaField<T>

    override var fullSuspendValidation: FullSuspendValidation<T>? = null

    lateinit var radioButtonGroup: RadioButtonGroup<T>

    override var itemBuilderFun: Z2.(value: T) -> Unit
        get() = radioButtonGroup.itemBuilderFun
        set(value) {
            radioButtonGroup.itemBuilderFun = value
        }

    override var queryFun: (suspend () -> List<T>)? = null
        set(value) {
            field = value
            if (value != null) localLaunch { radioButtonGroup.options = value() }
        }

    override var options: List<T>
        get() = radioButtonGroup.options
        set(value) {
            radioButtonGroup.options = value
        }

    override var readOnly: Boolean = false
        set(value) {
            field = value
            radioButtonGroup.readOnly = value
        }

    init {
        attach(schematic) {

            val value = field.getValue(schematic)
            radioButtonGroup.value = value

            if (readOnly) return@attach

            val schemaResult = it.validationResult.fieldResults[field.name]
            val valid = schemaResult?.valid ?: true

            if (fullSuspendValidation == null || ! valid) {
                radioButtonGroup.setState(! valid, schemaResult?.fails?.firstOrNull()?.message)
                return@attach
            }

            fullSuspendValidation?.let { validation ->
                io {
                    val result = validation(schematic, field, value)
                    // checking for the value so if the user changed it since we won't set it to an old check result
                    if (value == radioButtonGroup.value) radioButtonGroup.setState(! result.valid, result.fails.firstOrNull()?.message)
                }
            }
        }

        radioButtonGroup = RadioButtonGroup<T>(this).also {
            it.value = field.getValue(schematic)
            it.onSelectedFun = { value -> context.schematic.schematicChange(context.field, value) }
        }
    }

}