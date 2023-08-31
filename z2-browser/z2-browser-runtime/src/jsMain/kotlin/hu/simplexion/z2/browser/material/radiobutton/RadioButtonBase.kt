package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.html.onKeyDown
import hu.simplexion.z2.browser.material.StateLayer
import hu.simplexion.z2.browser.material.basicIcons
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

    lateinit var icon : Z2

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
        if (::icon.isInitialized) remove(icon)
        tabIndex = if (disabled) -1 else 0

        icon = icon(
            if (selected) basicIcons.radioButtonChecked else basicIcons.radioButtonUnchecked,
            size = 20
        ).also {
            it.zIndex = 1
            when {
                disabled -> it.addClass(onSurfaceText, opacity38)
                selected -> it.addClass(primaryText)
                else -> it.addClass(onSurfaceVariantText)
            }
        }
    }
}