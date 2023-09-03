package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.css.alignItemsCenter
import hu.simplexion.z2.browser.css.cursorPointer
import hu.simplexion.z2.browser.css.displayFlex
import hu.simplexion.z2.browser.css.pl8
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

class RadioButtonGroup<T>(
    parent: Z2,
    value: T,
    val entries: List<T>,
    val itemBuilder: (Z2.(T) -> Unit)? = null,
    val onChange: (value: T) -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    emptyArray()
) {
    val options = mutableListOf<RadioButtonBase>()

    var value = value
        set(value) {
            field = value
            for (index in entries.indices) {
                options[index].selected = (value == entries[index])
            }
        }

    // FIXME proper handling of readonly in RadioButtonGroup
    var readOnly : Boolean = false

    fun onSelected(entry: T) {
        value = entry
        onChange(entry)
    }

    init {
        build()
    }

    fun build() {
        for (entry in entries) {
            div(displayFlex, alignItemsCenter) {
                options += radioButton(entry == value, false) { onSelected(entry) }
                div(pl8, cursorPointer) {
                    itemBuilder?.let { it(entry) } ?: defaultItemBuilder(entry)
                    onClick { if (!readOnly) onSelected(entry) }
                }
            }
        }
    }

    fun Z2.defaultItemBuilder(entry: T) {
        addClass("body-middle")
        text { entry.toString() }
    }

    fun setState(error: Boolean, errorSupportingText: String? = null) {
        // FIXME radiobutton set state
    }
}