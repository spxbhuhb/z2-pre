package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.commons.browser.CssClass
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLInputElement

abstract class AbstractField<T>(
    parent: Z2,
    final override val state: FieldState = FieldState(),
    open val config: FieldConfig<T>
) : Z2(parent), ValueField<T> {

    lateinit var self: Z2
    lateinit var content: Z2
    lateinit var input: Z2
    lateinit var leading: Z2
    lateinit var trailing: Z2
    lateinit var label: Z2
    lateinit var support: Z2
    lateinit var animation: Z2

    var hasFocus = false

    val inError
        get() = state.error || state.invalidInput

    val inputElement
        get() = input.htmlElement as HTMLInputElement

    override var value: T
        get() = checkNotNull(valueOrNull)
        set(value) {
            valueOrNull = value
        }

    override var valueOrNull: T? = null
        get() {
            // this is here for Chrome autofill bug
            try {
                val inputValue = config.decodeFromString(inputElement.value)
                if (inputElement.value != "" && inputElement.value != field) {
                    field = inputValue
                }
            } catch (ex: Exception) {
                //
            }
            return field
        }
        set(value) {
            field = value

            try {
                val inputValue = config.decodeFromString(inputElement.value)
                if (inputValue != value) {
                    inputElement.value = value?.let { config.encodeToString(value) } ?: ""
                    state.invalidInput = false // direct change, should not be wrong
                    update()
                }
            } catch (ex: Exception) {
                // nothing to do here
            }
        }

    override fun main(): AbstractField<T> {
        addCss(displayGrid, minWidth0, boxSizingBorderBox, positionRelative)

        val baseHeight = if (config.style == FieldStyle.Chip) 32.px else 56.px

        gridTemplateColumns = "minmax(0,1fr)"
        gridTemplateRows = "$baseHeight min-content"

        grid(pl0) {
            gridTemplateColumns = "min-content minmax(0, 1fr) min-content"
            gridTemplateRows = baseHeight

            self = this

            label = div(positionAbsolute, pl14, bodySmall) {
                htmlElement.style.top = 8.px
                text { state.label }
            }

            leading = div(alignSelfCenter) { if (config.style == FieldStyle.Transparent) addCss(pt16) }
            content = div(alignSelfCenter) { input() }
            trailing = div(pr12, alignSelfCenter) { if (config.style == FieldStyle.Transparent) addCss(pt16) }

            when (config.style) {
                FieldStyle.Filled -> {
                    self.addCss(
                        borderBottomWidth1, borderBottomSolid,
                        borderColorOutline,
                        surfaceContainerHighest,
                        borderTopLeftRadiusExtraSmall,
                        borderTopRightRadiusExtraSmall,
                        pt2,
                        pr2,
                        pb2,
                    )
                }

                FieldStyle.Transparent -> {
                    self.addCss(
                        pt2,
                        pr2,
                        pb2,
                        borderBottomWidth1, borderBottomSolid
                    )
                    input.addCss(pt20)
                }

                FieldStyle.Outlined -> {
                    self.addCss(
                        pt2,
                        pr2,
                        pb1,
                        borderWidth1, borderSolid, borderColorOutline,
                        borderRadiusExtraSmall
                    )
                }

                FieldStyle.Chip -> {
                    self.addCss(
                        borderWidth1, borderSolid, borderColorOutline,
                        borderRadius8,
                        labelLarge
                    )
                    label.addCss(displayNone)
                }
            }

            onClick {
                if (! hasFocus) input.focus()
            }
        }

        // focus animation
        div(positionAbsolute, backgroundTransparent, boxSizingBorderBox, displayNone) {
            animation = this
            htmlElement.style.apply {
                borderStyle = "solid"
                when (config.style) {
                    FieldStyle.Outlined -> {
                        height = baseHeight
                        borderWidth = 2.px
                        addCss(borderRadiusExtraSmall, pointerEventsNone)
                    }

                    FieldStyle.Chip -> {
                        height = baseHeight
                        borderWidth = 2.px
                        addCss(borderRadius8, pointerEventsNone)
                    }

                    else -> {
                        top = 46.px
                        height = 10.px
                        borderWidth = 0.px
                        borderBottomWidth = 2.px
                    }
                }
            }
            zIndex = 1
            onClick {
                if (! hasFocus) input.focus()
            }
        }

        support()

        state.update = { update() }
        config.update = { update() }
        update()

        return this
    }

    fun Z2.input() {
        input(
            boxSizingBorderBox,
            outlineNone,
            borderNone,
            widthFull,
            heightFull,
            pl14,
            pr14,
            alignSelfCenter,
            justifyContentFlexStart,
            backgroundTransparent,
            onSurfaceText,
            caretColorPrimary
        ) {
            input = this

            if (config.style == FieldStyle.Chip) {
                input.addCss(labelLarge)
                inputElement.style.lineHeight = 24.px
            } else {
                input.addCss(bodyLarge)
                inputElement.style.lineHeight = 40.px
            }

            onFocus {
                if (! hasFocus) {
                    animation.removeCss(displayNone)
                    animation.addCss(CssClass("scale-up-width"), if (state.error) borderColorError else borderColorPrimary)
                }
                hasFocus = true
                update()
            }
            onBlur {
                hasFocus = false
                animation.addCss(displayNone)
                animation.removeCss(CssClass("scale-up-width"), borderColorError, borderColorPrimary)
                update()
            }
            onKeyDown { event ->
                when (event.key) {
                    "Enter" -> config.onEnter?.invoke()
                    "Escape" -> config.onEscape?.invoke()
                }
            }
            onInput {
                this@AbstractField.onInput()
            }
        }
    }

    open fun onInput() {
        if (! state.readOnly && ! state.disabled) {
            state.touched = true
            try {
                valueOrNull = config.decodeFromString(inputElement.value)
                state.invalidInput = false
                config.onChange?.invoke(this@AbstractField)
            } catch (ex: Exception) {
                console.warn("invalid input: ${inputElement.value}")
                ex.printStackTrace()
                state.invalidInput = true
            }
            update()
        }
    }

    fun Z2.support() =
        div(bodySmall, h20, pt4, pr16, pb0, pl16) {
            support = this
        }

    open fun update() {
        if (inputElement.value.isEmpty() && ! hasFocus) {
            label.addCss(displayNone)
            if (config.style != FieldStyle.Transparent) input.removeCss(pt20)
        } else {
            if (config.style != FieldStyle.Chip) {
                label.removeCss(displayNone)
                if (config.style != FieldStyle.Transparent) input.addCss(pt20)
            }
        }

        if (state.disabled) inputElement.disabled = true
        if (state.readOnly) inputElement.readOnly = true

        setLabel()

        inputElement.placeholder = if (hasFocus) "" else state.label ?: ""

        leading.clear()

        val li = config.leadingIcon
        if (li == null) {
            label.htmlElement.style.left = 0.px
        } else {
            label.htmlElement.style.left = 36.px
            leading.icon(li).addCss(pl12, onSurfaceVariantText)
        }

        if (config.style == FieldStyle.Filled) {
            if (inputElement.value.isEmpty() && ! hasFocus) {
                input.removeCss(pt20)
            } else {
                input.addCss(pt20)
            }
        }

        trailing.clear()
        support.clear()

        if (state.supportEnabled) {
            support.removeCss(displayNone)
        } else {
            support.addCss(displayNone)
        }

        if (state.touched && inError) {
            label.replaceCss(primaryText, onSurfaceVariantText, errorText)
            self.replaceCss(borderColorOutline, borderColorPrimary, borderColorError)
            animation.replaceCss(borderColorPrimary, borderColorError)
            inputElement.addClass("input-error")
            support.span(errorText) { + (state.errorText ?: state.supportText) }
            trailing.icon(config.errorIcon, fill = 1).addCss(errorText)
        } else {
            label.replaceCss(errorText, primaryText, onSurfaceVariantText, if (hasFocus) primaryText else onSurfaceVariantText)
            if (hasFocus) {
                self.replaceCss(borderColorError, borderColorOutline, borderColorPrimary)
            } else {
                self.replaceCss(borderColorError, borderColorPrimary, borderColorOutline)
            }
            animation.replaceCss(borderColorError, borderColorPrimary)
            inputElement.removeClass("input-error")
            support.span(onSurfaceVariantText) { + state.supportText }
            config.trailingIcon?.let { trailing.icon(it).addCss(onSurfaceVariantText) }
        }
    }

    fun setLabel() {
        val readOnly = if (state.disabled || state.readOnly) " (${browserStrings.readOnly})" else ""
        label.htmlElement.innerText = (state.label ?: "") + readOnly
    }
}