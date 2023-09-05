package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.html.Z2

fun Z2.radioButton(selected: Boolean, disabled: Boolean, onSelect: () -> Unit) =
    RadioButtonBase(this, selected, disabled, onSelect)

fun <T> Z2.radioButtonGroup(value: T, options: List<T>, itemBuilderFun: (Z2.(item: T) -> Unit)? = null, onChange: (value: T) -> Unit) =
    RadioButtonGroup<T>(this).also {
        it.value = value
        if (itemBuilderFun != null) it.itemBuilderFun = itemBuilderFun
        it.onSelectedFun = onChange
        it.options = options
    }