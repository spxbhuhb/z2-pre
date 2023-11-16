package hu.simplexion.z2.browser.material.button

import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.css.labelLarge
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.localization.text.LocalizedText

fun Z2.segmentedButton(
    vararg segments: Pair<LocalizedText, Boolean>,
    onClick: (selected: LocalizedText) -> Unit
) =
    Button(this, arrayOf("segmented-button-container".css), { }) {
        for (segment in segments) {
            div("segmented-button".css, labelLarge, if (segment.second) "selected".css else "unselected".css) {
                text { segment.first }
                this.onClick { onClick(segment.first) }
            }
        }
    }