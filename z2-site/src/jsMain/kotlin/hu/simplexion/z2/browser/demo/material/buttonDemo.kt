package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.immaterial.button.filledSelectButton
import hu.simplexion.z2.browser.immaterial.button.textSelectButton
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.button.*
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.snackbar.snackbar
import kotlinx.coroutines.delay

fun Z2.buttonDemo() =

    surfaceContainerLow {
        grid {
            gridTemplateColumns = "min-content"
            gridAutoRows = "min-content"
            gridGap = "16px"

            var counter = 1

            filledButton(strings.filledButton) { snackbar("${strings.filledButton} ${counter++}") }
            filledLaunchButton(strings.filledLaunchButton) {
                snackbar("${strings.filledLaunchButton} ${counter++}")
                delay(3000)
            }

            textButton(strings.textButton) { snackbar("${strings.textButton} ${counter++}") }
            textLaunchButton(strings.textLaunchButton) {
                snackbar("${strings.textLaunchButton} ${counter++}")
                delay(3000)
            }

            smallDenseTextButton(strings.smallDenseTextButton) { snackbar("${strings.smallDenseTextButton} ${counter++}") }
            smallDenseTextLaunchButton(strings.smallDenseTextLaunchButton) {
                snackbar("${strings.smallDenseTextLaunchButton} ${counter++}")
                delay(3000)
            }

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

            val items = listOf(strings.item1, strings.item2, strings.item3)

            filledSelectButton(items, items[0]) {  }
            textSelectButton(items, items[0]) {  }
        }
    }