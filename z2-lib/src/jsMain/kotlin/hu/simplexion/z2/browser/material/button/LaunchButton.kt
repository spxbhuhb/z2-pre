package hu.simplexion.z2.browser.material.button

import hu.simplexion.z2.browser.css.labelLarge
import hu.simplexion.z2.browser.css.minWidthAuto
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.html.onMouseDown
import hu.simplexion.z2.deprecated.browser.CssClass
import hu.simplexion.z2.util.localLaunch
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

open class LaunchButton(
    parent : Z2? = null,
    classes : Array<out CssClass>,
    onClickFun: suspend (event : Event) -> Unit,
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

        addCss(labelLarge, minWidthAuto)

        onClick {
            if (isDisabled) return@onClick
            isDisabled = true
            localLaunch {
                onClickFun(it)
                isDisabled = false
            }
        }

        onMouseDown { it.preventDefault() } // to prevent the focus on the button
    }
}