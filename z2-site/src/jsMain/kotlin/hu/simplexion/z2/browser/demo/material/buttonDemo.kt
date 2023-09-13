package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.button.*
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.snackbar.snackbar

fun Z2.buttonDemo() =

    low {
        grid {
            gridTemplateColumns = "min-content"
            gridAutoRows = "min-content"
            gridGap = "16px"

            filledButton(strings.filledButton) { snackbar(strings.filledButton) }
            textButton(strings.textButton) { snackbar(strings.textButton) }
            smallDenseTextButton(strings.smallDenseTextButton) { snackbar(strings.smallDenseTextButton) }

            grid {
                gridTemplateRows = "min-content"
                gridTemplateColumns = "min-content min-content"
                gridGap = 16.px
                iconButton(browserIcons.settings, strings.settings) { snackbar(strings.settings) }
                outlinedIconButton(browserIcons.settings, strings.settings) { snackbar(strings.settings) }
            }

            segmentedButton(
                strings.segment1 to false,
                strings.segment2 to true,
                strings.segment3 to false
            ) { snackbar("${strings.click}: $it") }
        }
    }