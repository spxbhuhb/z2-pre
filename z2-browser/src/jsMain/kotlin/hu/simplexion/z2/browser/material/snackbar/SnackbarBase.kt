package hu.simplexion.z2.browser.material.snackbar

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.bodyMedium
import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.actionIcon
import hu.simplexion.z2.browser.util.io
import hu.simplexion.z2.localization.text.LocalizedText
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

class SnackbarBase(
    val message: String,
    val label: LocalizedText?,
    val icon: Boolean,
    val action: ((event: Event) -> Unit)?
) : Z2(
    null,
    document.createElement("div") as HTMLElement,
    arrayOf("snackbar".css),
    null
) {

    init {

        div(bodyMedium) { text { message } }

        action?.let {
            label?.let { textButton(label, action) }
        }

        if (icon) {
            actionIcon(browserIcons.close) { Snackbars.onHide(this) }
        }
    }

    fun show() {
        io {
            addClass("fade-in-bottom")
            Snackbars.append(this)
            delay(2000)
            Snackbars.onHide(this)
        }
    }
}