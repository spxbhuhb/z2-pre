package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.FieldConfig.Companion.defaultFieldStyle
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

fun Z2.filledTextField(
    value: String,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    leadingIcon: LocalizedIcon? = null,
    trailingIcon: LocalizedIcon? = null,
    onChange: (value: AbstractField<String>) -> Unit = { }
) =
    textField(value, FieldStyle.Filled, label, supportingText, leadingIcon, trailingIcon, onChange)


fun Z2.transparentTextField(
    value: String,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    leadingIcon: LocalizedIcon? = null,
    trailingIcon: LocalizedIcon? = null,
    onChange: (value: AbstractField<String>) -> Unit = { }
) =
    textField(value, FieldStyle.Transparent, label, supportingText, leadingIcon, trailingIcon, onChange)

fun Z2.outlinedTextField(
    value: String,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    leadingIcon: LocalizedIcon? = null,
    trailingIcon: LocalizedIcon? = null,
    onChange: (value: AbstractField<String>) -> Unit = { }
) =
    textField(value, FieldStyle.Outlined, label, supportingText, leadingIcon, trailingIcon, onChange)

fun Z2.textField(
    value: String,
    style: FieldStyle = defaultFieldStyle,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    leadingIcon: LocalizedIcon? = null,
    trailingIcon: LocalizedIcon? = null,
    onChange: (value: AbstractField<String>) -> Unit = { }
) =
    TextField(
        this,
        FieldState().also {
            it.label = label?.toString()
            it.supportText = supportingText?.toString()
        },
        FieldConfig(
            style,
            { it }
        ).also {
            it.leadingIcon = leadingIcon
            it.trailingIcon = trailingIcon
            it.onChange = onChange
        }
    ).also {
        it.main()
        it.value = value
    }