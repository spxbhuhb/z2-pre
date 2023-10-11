package hu.simplexion.z2.browser.material.card

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText

fun Z2.elevatedCard(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
    builder: Z2.() -> Unit
): Z2 =

    div("elevated-card-container") {
        if (headline != null) cardHeadline(headline, actions)
        builder()
    }

fun Z2.filledCard(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
    builder: Z2.() -> Unit
): Z2 =

    div("filled-card-container") {
        if (headline != null) cardHeadline(headline, actions)
        builder()
    }

fun Z2.outlinedCard(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
    builder: Z2.() -> Unit
): Z2 =

    div("outlined-card-container") {
        if (headline != null) cardHeadline(headline, actions)
        builder()
    }


fun Z2.cardHeadline(
    headline: LocalizedText? = null,
    actions: (Z2.() -> Unit)? = null,
): Z2 =
    div("card-headline", "title-large") {

        div("align-self-center") {
            text { headline?.localeCapitalized }
        }

        actions?.let { it() }
    }
