package hu.simplexion.z2.browser.demo.layout

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.*
import hu.simplexion.z2.browser.material.px

fun Z2.containerDemo() =
    div {
        grid {
            gridTemplateColumns = "repeat(5, 1fr)"
            gridAutoRows = "min-content"
            gridGap = 16.px

            div(titleMedium) {
                gridColumn = "1/6"
                text { "Default Padding" }
                span(labelSmall, pl8) { text { "(16 px)" } }
            }

            surfaceContainerLowest(borderPrimary) { content { "lowest" } }
            surfaceContainerLow(borderPrimary) { content { "low" } }
            surfaceContainer(borderPrimary) { content { "container" } }
            surfaceContainerHigh(borderPrimary) { content { "high" } }
            surfaceContainerHighest(borderPrimary) { content { "highest" } }

            div(titleMedium) {
                gridColumn = "1/6"
                text { "No Padding" }
            }

            surfaceContainerLowest(borderPrimary, p0) { content { "lowest" } }
            surfaceContainerLow(borderPrimary, p0) { content { "low" } }
            surfaceContainer(borderPrimary, p0) { content { "container" } }
            surfaceContainerHigh(borderPrimary, p0) { content { "high" } }
            surfaceContainerHighest(borderPrimary, p0) { content { "highest" } }
        }
    }

internal fun Z2.content(text: () -> String) =
    div(labelMedium, "primary-text", textTransformUppercase) {
        style.height = 60.px
        text { text() }
    }