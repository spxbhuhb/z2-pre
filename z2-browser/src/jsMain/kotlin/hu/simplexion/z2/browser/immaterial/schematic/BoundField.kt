package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.util.io
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.schematic.SchematicFieldEvent
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.schema.SchemaField

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

            val validationResult = when (it) {
                is Touch -> {
                    uiField.state.touched = true
                    it.validationResult
                }

                is SchematicFieldEvent -> {
                    it.validationResult
                }

                else -> return@attachListener
            }

            val value = schemaField.getValue(schematic) as T?
            uiField.valueOrNull = value

            if (uiField.state.readOnly) return@attachListener

            val schemaResult = validationResult.fieldResults[schemaField.name]
            val valid = schemaResult?.valid ?: true

            if (fullSuspendValidation == null) {
                uiField.state.error = ! valid
                uiField.state.errorText = schemaResult?.fails?.firstOrNull()?.message
                return@attachListener
            }

            fullSuspendValidation?.let { validation ->
                io {
                    val result = validation(schematic, schemaField as SchemaField<T>, value)
                    // checking for the value so if the user changed it since we won't set it to an old check result
                    if (value == uiField.value) {
                        uiField.state.error = ! result.valid
                        uiField.state.errorText = result.fails.firstOrNull()?.message
                    }
                }
            }
        }

        uiField.valueOrNull = schemaField.getValue(schematic) as T?
    }

    infix fun validateSuspend(validation: SuspendValidation<T>) {
        fullSuspendValidation = { _, _, value -> validation(value) }
    }

    infix fun validateSuspendFull(validation: FullSuspendValidation<T>) {
        fullSuspendValidation = validation
    }

    infix fun readOnly(value: Boolean)  : BoundField<T> {
        readOnly = value
        return this
    }

    var readOnly
        get() = uiField.state.readOnly
        set(value) {
            uiField.state.readOnly = value
        }

    infix fun disabled(value: Boolean) : BoundField<T> {
        disabled = value
        return this
    }

    var disabled
        get() = uiField.state.disabled
        set(value) {
            uiField.state.disabled = value
        }

    infix fun label(value: String) : BoundField<T> {
        label = value
        return this
    }

    infix fun label(value: LocalizedText?) : BoundField<T> {
        label = value?.toString() ?: ""
        return this
    }

    var label
        get() = uiField.state.label
        set(value) {
            uiField.state.label = value
        }

    infix fun supportEnabled(value: Boolean) : BoundField<T> {
        supportEnabled = value
        return this
    }

    var supportEnabled
        get() = uiField.state.supportEnabled
        set(value) {
            uiField.state.supportEnabled = value
        }


    infix fun supportText(value: String) : BoundField<T> {
        supportText = value
        return this
    }

    infix fun supportText(value: LocalizedText?) : BoundField<T> {
        supportText = value?.toString() ?: ""
        return this
    }

    var supportText
        get() = uiField.state.supportText
        set(value) {
            uiField.state.supportText = value
        }

    infix fun error(value: Boolean) : BoundField<T> {
        error = value
        return this
    }

    var value
        get() = uiField.value
        set(value) {
            uiField.value = value
        }

    var valueOrNull
        get() = uiField.valueOrNull
        set(value) {
            uiField.valueOrNull = valueOrNull
        }

    var error
        get() = uiField.state.error
        set(value) {
            uiField.state.error = value
        }

    infix fun errorText(value: String) : BoundField<T> {
        errorText = value
        return this
    }

    var errorText
        get() = uiField.state.errorText
        set(value) {
            uiField.state.errorText = value
        }

    var touched
        get() = uiField.state.touched
        set(value) { uiField.state.touched = value}

    fun touch() {
        uiField.state.touched = true
    }

    fun toNormal() {
        uiField.state.error = false
        uiField.state.errorText = null
    }

    fun toError(errorText : LocalizedText? = null) {
        uiField.state.toError(errorText)
    }
}