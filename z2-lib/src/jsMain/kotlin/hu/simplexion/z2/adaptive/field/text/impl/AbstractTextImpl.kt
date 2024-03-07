package hu.simplexion.z2.adaptive.field.text.impl

import hu.simplexion.z2.adaptive.browser.CssClass
import hu.simplexion.z2.adaptive.event.EventCentral
import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.EnterKeyEvent
import hu.simplexion.z2.adaptive.field.EscapeKeyEvent
import hu.simplexion.z2.adaptive.field.FieldRenderer
import hu.simplexion.z2.adaptive.field.RequestBlurEvent
import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.adaptive.impl.AdaptiveImpl
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.schematic.SchematicEvent
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLInputElement

/**
 * Common code for the these text renderers:
 *
 * - [BorderBottomTextImpl]
 * - [ChipTextImpl]
 * - [FilledTextImpl]
 * - [OutlinedTextImpl]
 */
abstract class AbstractTextImpl(
    parent: Z2,
) : Z2(parent), FieldRenderer<TextField, String>, AdaptiveImpl {

    override lateinit var field: TextField

    lateinit var mainContainer: Z2
    lateinit var inputContainer: Z2
    lateinit var input: Z2
    lateinit var leading: Z2
    lateinit var trailing: Z2
    lateinit var label: Z2
    lateinit var support: Z2
    lateinit var animation: Z2

    val inputElement
        get() = input.htmlElement as HTMLInputElement

    val isEmpty
        get() = fieldValue.valueOrNull.isNullOrEmpty()

    open val baseHeight = 56.px

    override fun main(): AbstractTextImpl {
        attach(fieldValue, fieldState, fieldConfig)

        addCss(displayGrid, minWidth0, boxSizingBorderBox, positionRelative)

        gridTemplateColumns = "minmax(0,1fr)"
        gridTemplateRows = "$baseHeight min-content"

        mainContainer()
        animation()
        support()

        patch()
        return this
    }

    override fun onSchematicEvent(event: Z2Event) {
        when (event) {
            is SchematicEvent -> patch()
            is RequestBlurEvent -> inputElement.blur()
        }
    }

    // ---- Main Container --------------------------------------------------------------

    fun Z2.mainContainer() {
        grid("min-content minmax(0, 1fr) min-content", baseHeight, pl0) {

            mainContainer = this
            mainContainerStyles()

            label = label()
            leading = leading()
            inputContainer = inputContainer()
            trailing = trailing()

            onClick {
                if (! fieldState.hasFocus) input.focus()
            }

        }
    }

    open fun Z2.label() =
        div(positionAbsolute, bodySmall) {
            htmlElement.style.top = 8.px
            htmlElement.style.paddingLeft = 15.px
            text { fieldConfig.label }
        }

    open fun Z2.leading() = div(alignSelfCenter)

    open fun Z2.inputContainer() = div(alignSelfCenter) { input() }

    open fun Z2.trailing() = div(pr12, alignSelfCenter)

    abstract fun mainContainerStyles()

    // ---- Animation --------------------------------------------------------------

    open fun Z2.animation() {
        div(positionAbsolute, backgroundTransparent, boxSizingBorderBox, displayNone) {
            animation = this
            animationStyles()

            zIndex = 1

            onClick {
                if (! fieldState.hasFocus) input.focus()
            }
        }
    }

    abstract fun animationStyles()

    // ---- Input --------------------------------------------------------------

    open fun Z2.input() {
        input(
            boxSizingBorderBox,
            outlineNone,
            borderNone,
            wFull,
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

            inputStyles()

            onFocus {
                if (! hasFocus) {
                    animation.removeCss(displayNone)
                    animation.addCss(CssClass("scale-up-width"), if (error) borderColorError else borderColorPrimary)
                }
                fieldState.hasFocus = true
            }

            onBlur {
                fieldState.hasFocus = false
                animation.addCss(displayNone)
                animation.removeCss(CssClass("scale-up-width"), borderColorError, borderColorPrimary)
            }

            onKeyDown { event ->
                when (event.key) {
                    "Enter" -> EventCentral.fire(EnterKeyEvent(fieldState.schematicHandle))
                    "Escape" -> EventCentral.fire(EscapeKeyEvent(fieldState.schematicHandle))
                }
            }

            onInput {
                this@AbstractTextImpl.onInput()
            }
        }
    }

    open fun inputStyles() {
        input.addCss(bodyLarge)
        inputElement.style.lineHeight = 40.px
    }

    open fun onInput() {
        if (! readOnly) {
            fieldValue.valueOrNull = inputElement.value
            fieldState.touched = true
        }
    }

    open fun Z2.support() =
        div(bodySmall, h20, pt4, pr16, pb0, pl16) {
            support = this
        }

    open fun patch() {
        patchMainContainer()
        patchAnimation()
        patchSupport()
    }

    open fun patchMainContainer() {
        when {
            error -> mainContainer.replaceCss(borderColorOutline, borderColorPrimary, borderColorError)
            hasFocus -> mainContainer.replaceCss(borderColorError, borderColorOutline, borderColorPrimary)
            else -> mainContainer.replaceCss(borderColorError, borderColorPrimary, borderColorOutline)
        }

        patchLabel()
        patchLeading()
        patchInput()
        patchTrailing()
    }

    open fun patchLabel() {
        if (isEmpty && ! hasFocus && ! readOnly) {
            hideLabel()
        } else {
            showLabel()
        }

        setLabel()

        if (error) {
            label.replaceCss(primaryText, onSurfaceVariantText, errorText)
        } else {
            label.replaceCss(errorText, primaryText, onSurfaceVariantText, if (hasFocus) primaryText else onSurfaceVariantText)
        }
    }

    open fun hideLabel() {
        label.addCss(displayNone)
        input.removeCss(pt20)
    }

    open fun showLabel() {
        label.removeCss(displayNone)
        input.addCss(pt20)
    }

    open fun setLabel() {
        label.htmlElement.innerText = (fieldConfig.label ?: "")
    }

    open fun patchLeading() {
        leading.clear()

        val li = fieldConfig.leadingIcon
        if (li == null) {
            label.htmlElement.style.left = 0.px
        } else {
            label.htmlElement.style.left = 36.px
            leading.icon(li).addCss(pl12, onSurfaceVariantText)
        }
    }

    open fun patchInput() {
        inputElement.readOnly = readOnly
        inputElement.placeholder = if (hasFocus || readOnly) "" else fieldConfig.label ?: ""

        val value = fieldValue.valueOrNull ?: ""
        if (inputElement.value != value) inputElement.value = value

        if (error) {
            inputElement.addClass("input-error")
        } else {
            inputElement.removeClass("input-error")
        }
    }

    fun patchTrailing() {
        trailing.clear()
        if (error) {
            fieldConfig.errorIcon?.let { trailing.icon(it, fill = 1).addCss(errorText) }
        } else {
            fieldConfig.trailingIcon?.let { trailing.icon(it).addCss(onSurfaceVariantText) }
        }
    }

    fun patchAnimation() {
        if (error) {
            animation.replaceCss(borderColorPrimary, borderColorError)
        } else {
            animation.replaceCss(borderColorError, borderColorPrimary)
        }
    }

    fun patchSupport() {
        support.clear()

        if (fieldConfig.supportEnabled) {
            support.removeCss(displayNone)
        } else {
            support.addCss(displayNone)
        }

        if (error) {
            support.span(errorText) { + (fieldState.errorText ?: fieldConfig.supportText) }
        } else {
            support.span(onSurfaceVariantText) { + fieldConfig.supportText }
        }
    }
}