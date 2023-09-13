package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.card.filledCard
import hu.simplexion.z2.browser.material.icon.actionIcon
import hu.simplexion.z2.browser.material.popup.PopupBase
import hu.simplexion.z2.browser.material.popup.popup
import hu.simplexion.z2.browser.material.px

fun Z2.popupDemo() =
    surfaceContainerLow {
        grid {
            gridTemplateColumns = 400.px
            gridAutoRows = "min-content"
            gridGap = 16.px

            div {
                var popup: PopupBase? = null
                textButton(strings.popup) {
                    popup?.toggle()
                    it.preventDefault()
                    it.stopPropagation() // this is necessary for buttons
                }.let {
                    addClass("position-relative", "popup-parent")
                    popup = popup { filledCard { text { strings.loremShort } } }
                }
            }

            div {
                var popup: PopupBase? = null
                actionIcon(browserIcons.settings) {
                    popup?.toggle()
                }.apply {
                    addClass("position-relative", "popup-parent")
                    popup = popup { filledCard { text { strings.loremShort } } }
                }
            }
        }
    }