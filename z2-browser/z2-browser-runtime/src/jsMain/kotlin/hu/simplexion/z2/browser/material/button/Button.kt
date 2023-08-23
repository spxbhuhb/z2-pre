package hu.simplexion.z2.browser.material.button

import hu.simplexion.z2.browser.css.labelLarge
import hu.simplexion.z2.browser.css.textTransformCapitalize
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.html.onMouseDown
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

class Button(
    parent : Z2? = null,
    classes : Array<out String>,
    onClickFun: (event : Event) -> Unit,
    builder: Z2.() -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes,
    builder
) {
    var isDisabled : Boolean = false
        set(value) {
            field = value
            if (value) {
                addClass("disabled")
            } else {
                removeClass("disabled")
            }
        }

    init {

        addClass(labelLarge, textTransformCapitalize)

        onClick {
            if (isDisabled) return@onClick
            onClickFun(it)
        }

        onMouseDown { it.preventDefault() } // to prevent the focus on the button
    }
}