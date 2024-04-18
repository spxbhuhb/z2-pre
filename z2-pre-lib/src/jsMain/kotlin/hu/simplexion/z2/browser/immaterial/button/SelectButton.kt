package hu.simplexion.z2.browser.immaterial.button

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.pr8
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.html.onMouseDown
import hu.simplexion.z2.browser.html.span
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.menu.dropdownMenu
import hu.simplexion.z2.browser.material.menu.menuItem
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.stateLayer
import hu.simplexion.z2.deprecated.browser.CssClass
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

open class SelectButton<T>(
    parent: Z2? = null,
    classes: Array<out CssClass>,
    var state: SelectButtonState<T>
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes
) {

    var isDisabled: Boolean = false
        set(value) {
            field = value
            if (value) {
                addClass("disabled")
            } else {
                removeClass("disabled")
            }
        }

    init {
        onClick {
            if (isDisabled) return@onClick
        }

        onMouseDown { it.preventDefault() } // to prevent the focus on the button

        update()
    }

    fun update() {
        clear()

        stateLayer().apply {
            style.marginLeft = "-20px"
            style.borderRadius = 20.px
        }

        span(pr8) { + state.value.toString() }
        icon(browserIcons.down)

        dropdownMenu {
            for (option in state.options) {
                menuItem(option, if (option == state.value) browserIcons.check else browserIcons.empty, option.toString()) {
                    state = state.copy(value = option)
                    update()
                }
            }
        }
    }
}