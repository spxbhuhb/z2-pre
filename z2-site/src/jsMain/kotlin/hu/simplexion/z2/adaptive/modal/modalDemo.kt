package hu.simplexion.z2.adaptive.modal

import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.textfield.outlinedTextField
import hu.simplexion.z2.localization.runtime.localized

fun modalDemo(container: Z2) {
    container.surfaceContainerLow(borderOutline) {
        textButton("big modal".localized) { bigModal() }
    }
}

private fun bigModal() {
    modal {
        title("Big modal")
        body {
            grid("max-content max-content", "1fr", gridGap24) {
                div {
                    for (i in 1..10) {
                        outlinedTextField("A $i")
                    }
                }
                div {
                    for (i in 1..10) {
                        outlinedTextField("A $i")
                    }
                }
            }
        }
        save {
            close()
        }
    }
}