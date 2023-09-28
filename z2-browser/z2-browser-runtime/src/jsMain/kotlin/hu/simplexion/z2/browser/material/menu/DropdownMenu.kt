package hu.simplexion.z2.browser.material.menu

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.popup.PopupBase
import hu.simplexion.z2.browser.material.popup.popup
import hu.simplexion.z2.browser.material.px

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
            grid(positionRelative, boxSizingBorderBox, displayGrid, pt8, pl0, pb8, pr0, borderRadius4, surfaceContainer, elevationLevel2) {
                gridAutoRows = "min-content"
                gridTemplateColumns = "1fr"
                style.minWidth = 112.px
                builder()
            }
        }
    }

}