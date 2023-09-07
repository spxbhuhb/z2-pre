package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

fun Z2.filledTextField(
    value: String,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    leadingIcon: LocalizedIcon? = null,
    trailingIcon: LocalizedIcon? = null,
    onChange : (value : String) -> Unit = {  }
) =
    FilledTextField(
        this,
        FieldState().also {
            it.label = label?.toString()
            it.supportText = supportingText?.toString()
        },
        TextFieldConfig().also {
            it.leadingIcon  = leadingIcon
            it.trailingIcon = trailingIcon
            it.onChange = onChange
        }
    ).also {
        it.value = value
    }