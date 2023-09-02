package hu.simplexion.z2.browser.material.menu

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

fun <T> Z2.menuItem(
    value: T,
    icon: LocalizedIcon? = null,
    label: LocalizedText,
    leading: Z2Builder? = null,
    trailing: Z2Builder? = null,
    disabled: Boolean = false,
    onSelected: (item : MenuItemBase<T>) -> Unit
) = MenuItemBase(this, value, icon, leading, label.toString(), trailing, disabled, onSelected)

fun <T> Z2.menuItem(
    value: T,
    icon: LocalizedIcon? = null,
    label: String,
    leading: Z2Builder? = null,
    trailing: Z2Builder? = null,
    disabled: Boolean = false,
    onSelected: (item : MenuItemBase<T>) -> Unit
) = MenuItemBase(this, value, icon, leading, label, trailing, disabled, onSelected)