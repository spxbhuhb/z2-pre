package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

fun Z2.filledTextField(
    value: String,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    leadingIcon: LocalizedIcon? = null,
    trailingIcon: LocalizedIcon? = null,
    state : ComponentState = ComponentState.Enabled,
    error : Boolean = false,
    onChange : TextField.(value : String) -> Unit = {  }
) =
    TextField(
        this,
        value,
        label,
        supportingText,
        filled = true,
        outlined = false,
        leadingIcon,
        trailingIcon,
        state,
        error,
        onChange = onChange
    )