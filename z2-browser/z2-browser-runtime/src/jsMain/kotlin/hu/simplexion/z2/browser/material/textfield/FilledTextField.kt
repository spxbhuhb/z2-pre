package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.px
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLInputElement

class FilledTextField(
    parent: Z2,
    val state: FieldState = FieldState(),
    val config: TextFieldConfig = TextFieldConfig()
) : Z2(parent), ValueField<String> {

    lateinit var main: Z2
    lateinit var content: Z2
    lateinit var input: Z2
    lateinit var leading: Z2
    lateinit var trailing: Z2
    lateinit var labelOuter: Z2
    lateinit var support: Z2
    lateinit var animation: Z2

    var hasFocus = false

    override var value
        get() = inputElement.value
        set(value) {
            inputElement.value = value
            update()
        }

    val inputElement
        get() = input.htmlElement as HTMLInputElement

    init {
        addClass(displayGrid, minWidth0, boxSizingBorderBox, positionRelative)
        gridTemplateColumns = "minmax(0,1fr)"
        gridTemplateRows = "56px min-content"

        state.update = { update() }

        grid(
            paddingTop2,
            paddingRight2,
            paddingBottom1,
            paddingLeft0,
            borderTopLeftRadiusExtraSmall,
            borderTopRightRadiusExtraSmall,
            surfaceContainerHighest
        ) {
            gridTemplateColumns = "min-content minmax(0, 1fr) min-content"
            gridTemplateRows = "56px"

            main = this

            label()

            leadingIcon()
            div(alignSelfCenter) {
                content = this
                input()
            }
            trailing()
        }

        div(positionAbsolute) {
            animation = this
            htmlElement.style.apply {
                top = 54.px
                height = 2.px
            }
            zIndex = 1
        }

        support()

        update()
    }

    fun Z2.label() =
        div(positionAbsolute, paddingLeft14, bodySmall) {
            htmlElement.style.top = 8.px
            labelOuter = this
            text { state.label }
        }

    fun Z2.leadingIcon(): Z2 =
        div {
            leading = this
            config.leadingIcon?.let {
                addClass(pl12, alignSelfCenter, onSurfaceVariant)
                icon(it)
            }
        }

    fun Z2.input() {
        input(
            boxSizingBorderBox,
            outlineNone,
            borderNone,
            widthFull,
            heightFull,
            paddingLeft14,
            paddingRight14,
            alignSelfCenter,
            justifyContentFlexStart,
            backgroundTransparent,
            onSurfaceText,
            caretColorPrimary,
            bodyLarge
        ) {
            input = this

            inputElement.style.lineHeight = 40.px

            onFocus {
                hasFocus = true
                animation.addClass("scale-up-width", if (state.error) "error" else primary)
                update()
            }
            onBlur {
                hasFocus = false
                animation.removeClass("scale-up-width")
                update()
            }
            onKeyDown { event ->
                when (event.key) {
                    "Enter" -> config.onEnter?.invoke()
                    "Escape" -> config.onEscape?.invoke()
                }
            }
            onInput {
                if (! state.readOnly && ! state.disabled) {
                    config.onChange?.invoke(inputElement.value)
                }
            }
        }
    }

    fun Z2.trailing(): Z2 =
        div(pr12, alignSelfCenter) {
            trailing = this
        }

    fun Z2.support() =
        div(bodySmall, h20, pt4, pr16, pb0, pl16) {
            support = this
        }

    fun update() {
        if (value.isEmpty() && ! hasFocus) {
            labelOuter.addClass(displayNone)
            input.removeClass(pt20)
        } else {
            labelOuter.removeClass(displayNone)
            input.addClass(pt20)
        }

        if (state.disabled) inputElement.disabled = true
        if (state.readOnly) inputElement.readOnly = true

        inputElement.placeholder = if (hasFocus) "" else state.label ?: ""

        if (state.error) {
            labelOuter.replaceClass(primaryText, onSurfaceVariantText, errorText)
            animation.replaceClass(primary, error)
            main.replaceClass(borderBottomOutline, borderBottomError)
            inputElement.addClass("input-error")
            supportError()
            trailingError()
        } else {
            labelOuter.replaceClass(errorText, primaryText, onSurfaceVariantText, if (hasFocus) primaryText else onSurfaceVariantText)
            animation.replaceClass(error, primary)
            main.replaceClass(borderBottomError, borderBottomOutline)
            inputElement.removeClass("input-error")
            supportNormal()
            trailingNormal()
        }
    }

    fun trailingNormal() {
        trailing.clear()
        config.trailingIcon?.let {
            trailing.icon(it).addClass(onSurfaceVariantText)
        }
    }

    fun trailingError() {
        trailing.clear()
        trailing.icon(config.errorIcon, fill = 1).addClass(errorText)
    }

    fun supportNormal() {
        support.clear()
        support.span(onSurfaceVariantText) { + state.supportText }
    }

    fun supportError() {
        support.clear()
        support.span(errorText) { + (state.errorText ?: state.supportText) }
    }
}