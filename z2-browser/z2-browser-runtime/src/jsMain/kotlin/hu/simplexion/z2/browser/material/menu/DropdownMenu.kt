package hu.simplexion.z2.browser.material.menu

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.material.popup.PopupBase
import hu.simplexion.z2.browser.material.popup.popup

class DropdownMenu(
    anchor: Z2,
    val inline: Boolean = false,
    val builder: Z2.() -> Unit
) {

    lateinit var popup: PopupBase

    init {
        anchor.addClass("popup-parent")
        anchor.onClick { popup.toggle() }

        popup = anchor.popup {
            grid("menu") {
                builder()
            }
        }
    }

}