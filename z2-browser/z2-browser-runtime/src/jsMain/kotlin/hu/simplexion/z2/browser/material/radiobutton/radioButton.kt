package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.material.StateLayer
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.icon.icon
import org.w3c.dom.events.MouseEvent

fun Z2.radioButton(
    selected: Boolean,
    disabled: Boolean,
    onClickFun: (event: MouseEvent) -> Unit
) =
    div(w40, h40, displayFlex, alignItemsCenter, justifyContentCenter, positionRelative) {

        if (!disabled) {
            tabIndex = 0
            StateLayer(this, arrayOf(w40, h40, borderRadius20))
            onClick { onClickFun(it as MouseEvent) }
        }

        icon(
            if (selected) basicIcons.radioButtonChecked else basicIcons.radioButtonUnchecked,
            size = 20
        ).also {
            when {
                disabled -> addClass(onSurfaceText, opacity38)
                selected -> primaryText
                else -> onSurfaceVariant
            }
        }
    }