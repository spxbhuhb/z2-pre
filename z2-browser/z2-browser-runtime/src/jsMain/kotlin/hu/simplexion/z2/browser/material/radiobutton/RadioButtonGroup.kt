package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.css.alignItemsCenter
import hu.simplexion.z2.browser.css.cursorPointer
import hu.simplexion.z2.browser.css.displayFlex
import hu.simplexion.z2.browser.css.pl8
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick

class RadioButtonGroup<T>(parent: Z2) : Z2(parent) {

    var onSelectedFun: ((value: T) -> Unit)? = null

    var itemBuilderFun: Z2.(T) -> Unit = { text { it } }
        set(value) {
            field = value
            build()
        }

    var options: List<T> = emptyList()
        set(value) {
            field = value
            build()
        }

    val buttons = mutableListOf<RadioButtonBase>()

    var value : T? = null
        set(value) {
            field = value
            for (index in options.indices) {
                buttons[index].selected = (value == options[index])
            }
        }

    // FIXME proper handling of readonly in RadioButtonGroup
    var readOnly: Boolean = false

    fun onSelected(entry: T) {
        value = entry
        onSelectedFun?.let { it(entry) }
    }

    fun build() {
        clear()
        for (entry in options) {
            div(displayFlex, alignItemsCenter) {
                buttons += radioButton(entry == value, false) { onSelected(entry) }
                div(pl8, cursorPointer) {
                    itemBuilderFun(entry)
                    onClick { if (! readOnly) onSelected(entry) }
                }
            }
        }
    }

    fun setState(error: Boolean, errorSupportingText: String? = null) {
        // FIXME radiobutton set state
    }
}