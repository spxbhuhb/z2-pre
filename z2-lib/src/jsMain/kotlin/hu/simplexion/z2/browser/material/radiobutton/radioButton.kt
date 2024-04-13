package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.FieldConfig.Companion.defaultFieldStyle
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText

fun Z2.radioButton(selected: Boolean, disabled: Boolean, onSelect: () -> Unit) =
    RadioButtonBase(this, selected, disabled, onSelect)

fun <T> Z2.radioButtonGroup(
    value: T?,
    options: List<T>,
    itemBuilderFun: (Z2.(item: T) -> Unit)? = null,
    onChange: RadioButtonGroup<T>.(value: T) -> Unit
) =
    RadioButtonGroup(
        this,
        FieldState(),
        RadioGroupConfig(
            defaultFieldStyle,
            itemBuilderFun,
            options,
            onChange
        )
    ).also {
        it.valueOrNull = value
    }

fun <T> Z2.radioButtonGroup(
    value: T?,
    options: List<T>,
    label : LocalizedText,
    style: FieldStyle,
    itemBuilderFun: (Z2.(item: T) -> Unit)? = null,
    onChange: RadioButtonGroup<T>.(value: T) -> Unit
) =
    RadioButtonGroup(
        this,
        FieldState().also { it.label  = label.localeCapitalized },
        RadioGroupConfig(
            style,
            itemBuilderFun,
            options,
            onChange
        )
    ).also {
        it.valueOrNull = value
    }