package hu.simplexion.z2.browser.components.select

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

fun <T> Z2.select(
    options: List<T>,
    value: T? = null,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    leadingIcon: LocalizedIcon? = null,
    style: FieldStyle = FieldConfig.defaultFieldStyle,
    onChange: ((field: AbstractField<T>) -> Unit)? = null
) =
    SelectBase(
        this,
        FieldState().also {
            if (label != null) it.label = label.toString()
            if (supportingText != null) it.supportText = supportingText.toString()
        },
        SelectConfig(
            style = style,
            options = options,
            onSelectedFun = onChange
        ).also {
            it.leadingIcon = leadingIcon
            it.trailingIcon = browserIcons.down
        }
    ).main().also {
        if (value != null) it.value = value
    }