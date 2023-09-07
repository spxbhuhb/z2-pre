package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.textfield.FilledTextField
import hu.simplexion.z2.browser.material.textfield.filledTextField
import hu.simplexion.z2.browser.material.textfield.outlinedTextField
import hu.simplexion.z2.browser.material.textfield.transparentTextField

fun Z2.textFieldDemo() =
    low {
        grid {
            gridTemplateColumns = "300px 300px 300px"
            gridAutoRows = "min-content"
            gridGap = "16px"

            div { + "filledTextField" }
            div { + "transparentTextField" }
            div { + "outlinedTextField" }

            textFieldDemo(ComponentState.Enabled, false)
            textFieldDemo(ComponentState.Enabled, true)
        }
    }

fun FilledTextField.set(state: ComponentState, error: Boolean, value : String = "") : FilledTextField {
    this.state.disabled = state == ComponentState.Disabled
    this.state.error = error
    this.value = value
    return this
}

private fun Z2.textFieldDemo(state: ComponentState, error: Boolean) {
    filledTextField("", strings.label).set(state, error)
    transparentTextField("", strings.label).set(state, error)
    outlinedTextField("", strings.label, state = state, error = error) { setState(it.isBlank()) }

    filledTextField("", strings.label).set(state, error, "test value")
    transparentTextField("", strings.label).set(state, error, "test value")
    outlinedTextField("test value", strings.label, state = state, error = error) { setState(it.isBlank()) }

    filledTextField("", strings.label).set(state, error)
    transparentTextField("", strings.label).set(state, error)
    outlinedTextField("", strings.label, state = state, error = error) { setState(it.isBlank()) }.also { it.value = "test value"}

    filledTextField("", strings.label).set(state, error).also {
        it.config.leadingIcon = basicIcons.search
        it.config.trailingIcon = basicIcons.cancel
    }
    transparentTextField("", strings.label).set(state, error).also {
        it.config.leadingIcon = basicIcons.search
        it.config.trailingIcon = basicIcons.cancel
    }
    outlinedTextField("", strings.label, leadingIcon = basicIcons.search, trailingIcon = basicIcons.cancel, state = state, error = error) { setState(it.isBlank()) }

    filledTextField("", strings.label).set(state, error).also {
        it.state.supportText = strings.supportingText.toString()
    }
    transparentTextField("", strings.label).set(state, error).also {
        it.state.supportText = strings.supportingText.toString()
    }
    outlinedTextField("", strings.label, strings.supportingText, state = state, error = error) { setState(it.isBlank()) }
}