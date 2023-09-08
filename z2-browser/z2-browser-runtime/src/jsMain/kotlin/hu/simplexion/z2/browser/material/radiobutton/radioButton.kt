package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.TextFieldConfig.Companion.defaultFieldStyle

fun Z2.radioButton(selected: Boolean, disabled: Boolean, onSelect: () -> Unit) =
    RadioButtonBase(this, selected, disabled, onSelect)

fun <T> Z2.radioButtonGroup(
    value: T,
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
        it.value = value
    }
