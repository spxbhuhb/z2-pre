package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.browser.material.textfield.*

fun Z2.textFieldDemo() =
    surfaceContainerLow {
        grid {
            gridTemplateColumns = "300px 300px 300px 300px"
            gridAutoRows = "min-content"
            gridGap = "16px"

            div { + "filledTextField" }
            div { + "transparentTextField" }
            div { + "outlinedTextField" }
            div { + "chipTextField" }

            div {
                gridColumn = "1/5"
                + "use value '12' to set field to error"
            }

            textFieldDemo(ComponentState.Enabled, false)
            textFieldDemo(ComponentState.Enabled, true)
        }
    }

fun TextField.set(
    state: ComponentState, error: Boolean, value: String = "", onChange: (AbstractField<String>) -> Unit = {
        this.state.error = (it.value == "12")
        this.state.touched = true
    }
): TextField {
    this.state.disabled = state == ComponentState.Disabled
    this.state.error = error
    this.config.onChange = onChange
    this.value = value
    if (error) this.state.touched = true
    return this
}

private fun Z2.textFieldDemo(state: ComponentState, error: Boolean) {
    filledTextField("", strings.label).set(state, error)
    transparentTextField("", strings.label).set(state, error)
    outlinedTextField("", strings.label).set(state, error)
    chipTextField("", strings.label).set(state, error)

    filledTextField("", strings.label).set(state, error, "test value")
    transparentTextField("", strings.label).set(state, error, "test value")
    outlinedTextField("test value", strings.label).set(state, error, "test value")
    chipTextField("test value", strings.label).set(state, error, "test value")

    filledTextField("", strings.label).set(state, error)
    transparentTextField("", strings.label).set(state, error)
    outlinedTextField("", strings.label).set(state, error)
    chipTextField("", strings.label).set(state, error)

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
    chipTextField("", strings.label).set(state, error).also {
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
    chipTextField("", strings.label).set(state, error).also {
        it.state.supportText = strings.supportingText.toString()
    }
}