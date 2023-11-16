package hu.simplexion.z2.browser.material.card

import hu.simplexion.z2.browser.css.alignSelfCenter
import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.css.titleLarge
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText

fun Z2.elevatedCard(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
    builder: Z2.() -> Unit
): Z2 =

    div("elevated-card-container".css) {
        if (headline != null) cardHeadline(headline, actions)
        builder()
    }

fun Z2.filledCard(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
    builder: Z2.() -> Unit
): Z2 =

    div("filled-card-container".css) {
        if (headline != null) cardHeadline(headline, actions)
        builder()
    }

fun Z2.outlinedCard(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
    builder: Z2.() -> Unit
): Z2 =

    div("outlined-card-container".css) {
        if (headline != null) cardHeadline(headline, actions)
        builder()
    }


fun Z2.cardHeadline(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
): Z2 =
    div("card-headline".css, titleLarge) {

        div(alignSelfCenter) {
            text { headline?.localeCapitalized }
        }

        actions?.let { it() }
    }
