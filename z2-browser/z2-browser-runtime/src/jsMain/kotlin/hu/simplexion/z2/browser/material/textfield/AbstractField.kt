package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.px
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

    val inputElement
        get() = input.htmlElement as HTMLInputElement

    override fun main() : AbstractField<T> {
        addClass(displayGrid, minWidth0, boxSizingBorderBox, positionRelative)
        gridTemplateColumns = "minmax(0,1fr)"
        gridTemplateRows = "56px min-content"

        grid(
            paddingTop2,
            paddingRight2,
            paddingBottom1,
            paddingLeft0,
            borderTopLeftRadiusExtraSmall,
            borderTopRightRadiusExtraSmall
        ) {
            gridTemplateColumns = "min-content minmax(0, 1fr) min-content"
            gridTemplateRows = "56px"

            self = this

            label = div(positionAbsolute, paddingLeft14, bodySmall) {
                htmlElement.style.top = 8.px
                text { state.label }
            }

            leading = div(alignSelfCenter) { if (config.style == FieldStyle.Transparent) addClass(pt16) }
            content = div(alignSelfCenter) { input() }
            trailing = div(pr12, alignSelfCenter) { if (config.style == FieldStyle.Transparent) addClass(pt16) }

            when (config.style) {
                FieldStyle.Filled -> {
                    self.addClass(borderBottomWidth1, borderBottomSolid, borderColorOutline, surfaceContainerHighest)
                }

                FieldStyle.Transparent -> {
                    self.addClass(borderBottomWidth1, borderBottomSolid)
                    input.addClass(pt20)
                }

                FieldStyle.Outlined -> {
                    self.addClass(borderWidth1, borderSolid, borderColorOutline, borderBottomRightRadiusExtraSmall, borderBottomLeftRadiusExtraSmall)
                }
            }

            onClick {
                if (!hasFocus) input.focus()
            }
        }

        // underline animation
        div(positionAbsolute, backgroundTransparent, boxSizingBorderBox, displayNone) {
            animation = this
            htmlElement.style.apply {
                borderStyle = "solid"
                if (config.style == FieldStyle.Outlined) {
                    height = 56.px
                    borderWidth = 2.px
                    addClass(borderRadiusExtraSmall, pointerEventsNone)
                } else {
                    top = 46.px
                    height = 10.px
                    borderWidth = 0.px
                    borderBottomWidth = 2.px
                }
            }
            zIndex = 1
            onClick {
                if (!hasFocus) input.focus()
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
                if (!hasFocus) {
                    animation.removeClass(displayNone)
                    animation.addClass("scale-up-width", if (state.error) borderColorError else borderColorPrimary)
                }
                hasFocus = true
                update()
            }
            onBlur {
                hasFocus = false
                animation.addClass(displayNone)
                animation.removeClass("scale-up-width", borderColorError, borderColorPrimary)
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
                    config.onChange?.invoke(this@AbstractField)
                }
            }
        }
    }

    fun Z2.support() =
        div(bodySmall, h20, pt4, pr16, pb0, pl16) {
            support = this
        }

    open fun update() {
        if (inputElement.value.isEmpty() && ! hasFocus) {
            label.addClass(displayNone)
            if (config.style != FieldStyle.Transparent) input.removeClass(pt20)
        } else {
            label.removeClass(displayNone)
            if (config.style != FieldStyle.Transparent) input.addClass(pt20)
        }

        if (state.disabled) inputElement.disabled = true
        if (state.readOnly) inputElement.readOnly = true

        inputElement.placeholder = if (hasFocus) "" else state.label ?: ""

        leading.clear()

        val li = config.leadingIcon
        if (li == null) {
            label.htmlElement.style.left = 0.px
        } else {
            label.htmlElement.style.left = 36.px
            leading.icon(li).addClass(pl12, onSurfaceVariantText)
        }

        if (config.style == FieldStyle.Filled) {
            if (inputElement.value.isEmpty() && ! hasFocus) {
                input.removeClass(pt20)
            } else {
                input.addClass(pt20)
            }
        }

        trailing.clear()
        support.clear()

        if (config.supportEnabled) {
            support.removeClass(displayNone)
        } else {
            support.addClass(displayNone)
        }

        if (state.error) {
            label.replaceClass(primaryText, onSurfaceVariantText, errorText)
            self.replaceClass(borderColorOutline, borderColorPrimary, borderColorError)
            animation.replaceClass(borderColorPrimary, borderColorError)
            inputElement.addClass("input-error")
            support.span(errorText) { + (state.errorText ?: state.supportText) }
            trailing.icon(config.errorIcon, fill = 1).addClass(errorText)
        } else {
            label.replaceClass(errorText, primaryText, onSurfaceVariantText, if (hasFocus) primaryText else onSurfaceVariantText)
            if (hasFocus) {
                self.replaceClass(borderColorError, borderColorOutline, borderColorPrimary)
            } else {
                self.replaceClass(borderColorError, borderColorPrimary, borderColorOutline)
            }
            animation.replaceClass(borderColorError, borderColorPrimary)
            inputElement.removeClass("input-error")
            support.span(onSurfaceVariantText) { + state.supportText }
            config.trailingIcon?.let { trailing.icon(it).addClass(onSurfaceVariantText) }
        }
    }

}