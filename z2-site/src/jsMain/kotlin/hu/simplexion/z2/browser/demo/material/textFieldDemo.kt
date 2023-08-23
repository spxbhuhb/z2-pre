package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.textfield.filledTextField
import hu.simplexion.z2.browser.material.textfield.outlinedTextField

fun Z2.textFieldDemo() =
    low {
        grid {
            gridTemplateColumns = "300px 300px"
            gridAutoRows = "min-content"
            gridGap = "16px"

            textFieldDemo(ComponentState.Enabled, false)
            textFieldDemo(ComponentState.Enabled, true)
        }
    }

private fun Z2.textFieldDemo(state: ComponentState, error: Boolean) {
    filledTextField("", strings.label, state = state, error = error) { this.error = it.isBlank() }
    outlinedTextField("", strings.label, state = state, error = error) { this.error = it.isBlank() }

    filledTextField("", strings.label, leadingIcon = basicIcons.search, trailingIcon = basicIcons.cancel, state = state, error = error) { this.error = it.isBlank() }
    outlinedTextField("", strings.label, leadingIcon = basicIcons.search, trailingIcon = basicIcons.cancel, state = state, error = error) { this.error = it.isBlank() }

    filledTextField("", strings.label, strings.supportingText, state = state, error = error) { this.error = it.isBlank() }
    outlinedTextField("", strings.label, strings.supportingText, state = state, error = error) { this.error = it.isBlank() }
}