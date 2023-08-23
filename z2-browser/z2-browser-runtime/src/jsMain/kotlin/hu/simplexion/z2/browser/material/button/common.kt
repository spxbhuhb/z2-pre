package hu.simplexion.z2.browser.material.button

import hu.simplexion.z2.browser.css.labelLarge
import hu.simplexion.z2.browser.css.labelSmall
import hu.simplexion.z2.browser.css.textTransformCapitalize
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.i18n.LocalizedText
import org.w3c.dom.events.Event

fun Z2.filledButton(label : LocalizedText, onClick : (event : Event) -> Unit) =
    Button(this, arrayOf("button-filled", labelLarge, textTransformCapitalize), onClick) {
        text { label }
    }

fun Z2.textButton(label : LocalizedText, onClick : (event : Event) -> Unit) =
    Button(this, arrayOf("button-text", labelLarge, textTransformCapitalize), onClick) {
        text { label }
    }

fun Z2.smallDenseTextButton(label : LocalizedText, onClick : (event : Event) -> Unit) =
    Button(this, arrayOf("button-text", "dense", labelSmall, textTransformCapitalize), onClick) {
        text { label }
    }