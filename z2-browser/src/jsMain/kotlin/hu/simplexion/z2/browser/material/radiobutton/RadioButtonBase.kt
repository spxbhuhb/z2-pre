package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.html.onKeyDown
import hu.simplexion.z2.browser.material.StateLayer
import hu.simplexion.z2.browser.material.icon.icon
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

class RadioButtonBase(
    parent: Z2? = null,
    selected: Boolean,
    disabled: Boolean,
    val onSelected: () -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes = arrayOf(w40, h40, displayFlex, alignItemsCenter, justifyContentCenter, positionRelative, outlineNone)
) {

    val stateLayer = StateLayer(this, arrayOf(w40, h40, borderRadius20), disabled)

    val contentLayer = div { zIndex = 1 }

    var selected = selected
        set(value) {
            if (field != value) {
                field = value
                build()
            }
        }

    var disabled = disabled
        set(value) {
            if (field != value) {
                field = value
                build()
            }
        }

    init {
        build()

        onClick {
            if (!disabled) {
                focus()
                onSelected()
            }
        }

        onKeyDown { if (!disabled && it.key == " ") onSelected() }
    }

    fun build() {
        stateLayer.disabled = disabled

        contentLayer.clear()

        tabIndex = if (disabled) -1 else 0

        with(contentLayer) {
            icon(
                if (selected) browserIcons.radioButtonChecked else browserIcons.radioButtonUnchecked,
                size = 20
            ).also {
                when {
                    disabled -> it.addClass(onSurfaceText, opacity38)
                    selected -> it.addClass(primaryText)
                    else -> it.addClass(onSurfaceVariantText)
                }
            }
        }
    }
}