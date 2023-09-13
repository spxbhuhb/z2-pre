package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.confirm

fun Z2.modalDemo() =
    surfaceContainerLow {
        textButton(strings.confirmDialog) { confirm(strings.confirmDialog, strings.confirmMessage) { } }
    }