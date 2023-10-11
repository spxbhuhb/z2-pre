package hu.simplexion.z2.browser.material.button

import hu.simplexion.z2.browser.css.labelLarge
import hu.simplexion.z2.browser.css.labelSmall
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText
import org.w3c.dom.events.Event

fun Z2.filledButton(label: LocalizedText, onClick: (event: Event) -> Unit) =
    Button(this, arrayOf("button-filled", labelLarge), onClick) {
        text { label.localeCapitalized }
    }

fun Z2.filledLaunchButton(label: LocalizedText, onClickFun: suspend (event: Event) -> Unit) =
    LaunchButton(this, arrayOf("button-filled", labelLarge), { onClickFun(it) }) {
        text { label.localeCapitalized }
    }

fun Z2.textButton(label: LocalizedText, onClick: (event: Event) -> Unit) =
    textButton(label, true, onClick)

fun Z2.textButton(label: LocalizedText, capitalize : Boolean, onClick: (event: Event) -> Unit) =
    Button(this, arrayOf("button-text", labelLarge), onClick) {
        text { if (capitalize) label.localeCapitalized else label }
    }

fun Z2.textLaunchButton(label: LocalizedText, onClickFun: suspend (event: Event) -> Unit) =
    LaunchButton(this, arrayOf("button-text", labelLarge), { onClickFun(it) }) {
        text { label.localeCapitalized }
    }

fun Z2.smallDenseTextButton(label: LocalizedText, onClick: (event: Event) -> Unit) =
    Button(this, arrayOf("button-text", "dense", labelSmall), onClick) {
        text { label.localeCapitalized }
    }

fun Z2.smallDenseTextLaunchButton(label: LocalizedText, onClickFun: suspend (event: Event) -> Unit) =
    LaunchButton(this, arrayOf("button-text", "dense", labelSmall), { onClickFun(it) }) {
        text { label.localeCapitalized }
    }