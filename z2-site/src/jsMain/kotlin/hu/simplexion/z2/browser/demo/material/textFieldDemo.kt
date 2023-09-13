package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.browser.material.textfield.TextField
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

            div {
                gridColumn = "1/4"
                + "use value '12' to set field to error"
            }

            textFieldDemo(ComponentState.Enabled, false)
            textFieldDemo(ComponentState.Enabled, true)
        }
    }

fun TextField.set(state: ComponentState, error: Boolean, value: String = "", onChange: TextField.(String) -> Unit = { this.state.error = (it == "12") }): TextField {
    this.state.disabled = state == ComponentState.Disabled
    this.state.error = error
    this.config.onChange = onChange
    this.value = value
    return this
}

private fun Z2.textFieldDemo(state: ComponentState, error: Boolean) {
    filledTextField("", strings.label).set(state, error)
    transparentTextField("", strings.label).set(state, error)
    outlinedTextField("", strings.label).set(state, error)

    filledTextField("", strings.label).set(state, error, "test value")
    transparentTextField("", strings.label).set(state, error, "test value")
    outlinedTextField("test value", strings.label).set(state, error, "test value")

    filledTextField("", strings.label).set(state, error)
    transparentTextField("", strings.label).set(state, error)
    outlinedTextField("", strings.label).set(state, error)

    filledTextField("", strings.label).set(state, error).also {
        it.config.leadingIcon = browserIcons.search
        it.config.trailingIcon = browserIcons.cancel
    }
    transparentTextField("", strings.label).set(state, error).also {
        it.config.leadingIcon = browserIcons.search
        it.config.trailingIcon = browserIcons.cancel
    }
    outlinedTextField("", strings.label).set(state, error).also {
        it.config.leadingIcon = browserIcons.search
        it.config.trailingIcon = browserIcons.cancel
    }

    filledTextField("", strings.label).set(state, error).also {
        it.state.supportText = strings.supportingText.toString()
    }
    transparentTextField("", strings.label).set(state, error).also {
        it.state.supportText = strings.supportingText.toString()
    }
    outlinedTextField("", strings.label).set(state, error).also {
        it.state.supportText = strings.supportingText.toString()
    }
}