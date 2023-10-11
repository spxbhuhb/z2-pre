package hu.simplexion.z2.browser.immaterial.select

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText

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

fun <T> Z2.singleChipSelect(
    options: List<T>,
    value: T? = null,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = null,
    itemTextFun: ((value: T) -> String)? = null,
    onChange: ((field: AbstractField<T>) -> Unit)? = null
) =
    SelectBase(
        this,
        FieldState().also {
            if (label != null) it.label = label.localeCapitalized
            if (supportingText != null) it.supportText = supportingText.toString()
        },
        SelectConfig(
            style = FieldStyle.Chip,
            options = options,
            onSelectedFun = onChange
        ).also {
            itemTextFun?.let { f -> it.itemTextFun = f }
            it.trailingIcon = browserIcons.down
            it.singleChipSelect = true
        }
    ).main().also {
        if (value != null) it.value = value
    }