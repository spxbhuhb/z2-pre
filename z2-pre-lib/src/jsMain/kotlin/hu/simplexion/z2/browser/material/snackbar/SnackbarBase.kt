package hu.simplexion.z2.browser.material.snackbar

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.actionIcon
import hu.simplexion.z2.browser.util.io
import hu.simplexion.z2.localization.text.LocalizedText
import kotlinx.coroutines.delay
import org.w3c.dom.events.Event

class SnackbarBase(
    val message: String,
    val label: LocalizedText?,
    val icon: Boolean,
    val delay: Long = 5000,
    val action: ((event: Event) -> Unit)?
) : Z2(null) {

    init {
        addCss(
            displayFlex,
            flexDirectionRow,
            alignItemsCenter,
            borderRadiusExtraSmall,
            elevationLevel3,
            positionAbsolute,
            h48,
            pl16,
            pr16
        )

        zIndex = 10000

        with(style) {
            bottom = "20px"
            left = "20px"
            minWidth = "240px"
            animationDuration = "0.25s"
            animationFillMode = "both"
            backgroundColor = "var(--md-sys-color-inverse-surface)"
            color = "var(--md-sys-color-inverse-on-surface)"
        }

        div(bodyMedium) { text { message } }

        action?.let {
            label?.let { textButton(label, action) }
        }

        if (icon) {
            div(pl16) {
                actionIcon(browserIcons.close) { Snackbars.onHide(this@SnackbarBase) }
            }
        }
    }

    fun show() {
        io {
            addClass("fade-in-bottom")
            Snackbars.append(this)
            delay(delay)
            Snackbars.onHide(this)
        }
    }
}