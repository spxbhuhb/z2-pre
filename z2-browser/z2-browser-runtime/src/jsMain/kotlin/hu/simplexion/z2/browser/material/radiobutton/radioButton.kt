package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.html.Z2

fun Z2.radioButton(selected : Boolean, disabled : Boolean, onSelect : () -> Unit) =
    RadioButtonBase(this, selected, disabled, onSelect)

fun <T> Z2.radioButtonGroup(value : T, entries : List<T>, onChange : (value : T) -> Unit) =
    RadioButtonGroup(this, value, entries, onChange = onChange)