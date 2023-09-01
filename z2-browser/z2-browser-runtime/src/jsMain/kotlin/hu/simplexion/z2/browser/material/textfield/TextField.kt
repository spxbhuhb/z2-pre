package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.css.displayNone
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.clear
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement

class TextField(
    parent: Z2,
    value: String,
    val label: LocalizedText? = null,
    val supportingText: LocalizedText? = null,
    val filled: Boolean = false,
    val outlined: Boolean = false,
    val leadingIcon: LocalizedIcon? = null,
    val trailingIcon: LocalizedIcon? = null,
    var state: ComponentState = ComponentState.Enabled,
    error: Boolean = false,
    val onChange: TextField.(value: String) -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLDivElement,
    arrayOf("text-field"),
    null
) {

    lateinit var main: Z2
    lateinit var content: Z2
    lateinit var leading: Z2
    lateinit var trailing: Z2
    lateinit var labelOuter: Z2
    lateinit var labelInner: Z2
    lateinit var support: Z2

    var errorIcon: LocalizedIcon = basicIcons.error

    var error: Boolean = error
        set(value) {
            field = value
            setState(state)
        }

    val input = Z2(this, document.createElement("input") as HTMLInputElement, emptyArray(), {})

    var value
        get() = (input.htmlElement as HTMLInputElement).value
        set(value) {
            (input.htmlElement as HTMLInputElement).value = value
            if (value.isNotEmpty()) {
                showLabel()
            }
        }

    var beforeEditValue = value

    init {
        if (outlined) labelOutlined()

        div(*classes()) {
            main = this
            gridRow = "1"
            gridColumn = "1"
            leadingIcon()
            div("align-self-center") {
                content = this
                if (filled) labelFilled()
                input()
            }
            trailingIcon()
        }

        supportingText()

        onMouseDown {
            if (it.target != input) {
                input.focus()
                it.preventDefault()
            }
        }

        setState(state)
    }

    fun leave() {
        val inputElement = input.htmlElement as HTMLInputElement
        if (beforeEditValue != inputElement.value) {
            beforeEditValue = inputElement.value
            onChange(inputElement.value)
        }
    }

    fun classes(): Array<String> {
        val classes = mutableListOf("text-field-main")
        if (value.isEmpty()) classes += "empty"
        if (filled) classes += "filled"
        if (outlined) classes += "outlined"
        return classes.toTypedArray()
    }

    fun Z2.labelFilled() =
        div {
            labelOuter = this
            addClass("text-field-label-filled", "body-small")
            if (value.isEmpty()) addClass(displayNone)
            text { label }
        }

    fun Z2.labelOutlined() =
        div("text-field-label-outer") {
            labelOuter = this

            div("text-field-top-left-corner") {}

            div("text-field-label-inner", "body-small") {
                labelInner = this
                if (value.isNotEmpty()) labelOutlinedContent()
            }

            div("text-field-top-right-corner") {}
        }

    fun Z2.labelOutlinedContent() {
        htmlElement.clear()
        div("text-field-label-outlined-content") { text { label } }
    }

    fun Z2.leadingIcon(): Z2 =
        div {
            leading = this
            if (leadingIcon != null) {
                addClass("text-field-leading-icon")
                icon(leadingIcon)
            }
        }

    fun Z2.input() {
        val parent = if (filled) div { labelFilled() } else this

        parent.append(input)
        val inputElement = input.htmlElement as HTMLInputElement

        input.addClass("text-field-input", "body-large")
        inputElement.value = value
        label?.let { inputElement.placeholder = it.toString() }

        input.onMouseDown {
            if (inputElement.readOnly) it.preventDefault()
        }

        input.onFocus {
            setState(ComponentState.Focus)
            showLabel()
        }

        input.onBlur {
            if (inputElement.value.isEmpty()) {
                when {
                    filled -> labelOuter.addClass(displayNone)
                    outlined -> labelInner.clear()
                }
            }
            leave()
            setState(ComponentState.Enabled)
        }

        input.onKeyDown { event ->
            when (event.key) {
                "Enter" -> leave()
                "Escape" -> inputElement.value = beforeEditValue
            }
        }

        input.onInput {
            onChange(inputElement.value)
        }
    }

    fun showLabel() {
        when {
            filled -> labelOuter.removeClass(displayNone)
            outlined -> labelInner.labelOutlinedContent()
        }
    }

    fun Z2.trailingIcon(): Z2 =
        div {
            trailing = this
            trailingState()
        }

    fun trailingState() {
        trailing.clear()
        if (error) {
            trailingError()
        } else {
            trailing.removeClass("text-field-trailing-icon")
            trailingNormal()
        }
    }

    fun trailingError() {
        with(trailing) {
            addClass("text-field-trailing-icon")
            icon(errorIcon, fill = 1)
        }
    }

    fun trailingNormal() {
        with(trailing) {
            if (trailingIcon != null) {
                addClass("text-field-trailing-icon")
                icon(trailingIcon)
            }
        }
    }

    fun Z2.supportingText() =
        div("text-field-support", "body-small") {
            support = this
            text { supportingText }
        }

    fun setState(newState: ComponentState) {
        val oldClass = state.fieldClass
        val newClass = newState.fieldClass
        state = newState

        htmlElement.querySelectorAll("*").forEach {
            removeClass(oldClass)
            addClass(newClass)
            if (error) {
                addClass("field-error")
            } else {
                removeClass("field-error")
            }
        }

        trailingState()
    }

    val ComponentState.fieldClass
        get() = this.name.lowercase()
}