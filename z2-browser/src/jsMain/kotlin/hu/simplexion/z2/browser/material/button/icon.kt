package hu.simplexion.z2.browser.material.button

import hu.simplexion.z2.browser.css.bodySmall
import hu.simplexion.z2.browser.css.borderPrimary
import hu.simplexion.z2.browser.css.primaryText
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText
import org.w3c.dom.events.Event

fun Z2.iconButton(
    icon: LocalizedIcon,
    hint: LocalizedText,
    size : Int = 24,
    weight : Int = 400,
    fill : Int = 0,
    vararg classes : String,
    onClick : (event : Event) -> Unit
) =
    Button(this, arrayOf("icon-button", primaryText), onClick) {
        div("icon-button-active-indicator-with-text", "tooltip") {
            addClass(*classes)
            icon(icon, size, weight, fill)
            div("plain-tooltip", bodySmall) { text { hint } }
        }
    }

fun Z2.outlinedIconButton(
    icon: LocalizedIcon,
    hint: LocalizedText,
    size : Int = 24,
    weight : Int = 400,
    fill : Int = 0,
    onClick : (event : Event) -> Unit
) = iconButton(icon, hint, size, weight, fill, borderPrimary, onClick = onClick)