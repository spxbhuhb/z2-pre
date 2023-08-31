package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.css.*
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

    fun onSelected(entry: T) {
        value = entry
        onChange(entry)
    }

    init {
        build()
    }

    fun build() {
        for (entry in entries) {
            println("entry")
            div(displayFlex, alignItemsCenter) {
                options += radioButton(entry == value, false) { onSelected(entry) }
                div(pl8, cursorPointer) {
                    itemBuilder?.let { it(entry) } ?: defaultItemBuilder(entry)
                    onClick { onSelected(entry) }
                }
            }
        }
    }

    fun Z2.defaultItemBuilder(entry : T) {
        addClass("body-middle")
        text { entry.toString() }
    }
}