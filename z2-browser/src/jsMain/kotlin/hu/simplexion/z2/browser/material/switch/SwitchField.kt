package hu.simplexion.z2.browser.material.switch

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.material.icon.icon

class SwitchField(
    parent: Z2,
    override val state: FieldState,
    val config: SwitchConfig
) : Z2(parent), ValueField<Boolean> {

    override var value: Boolean
        get() = checkNotNull(valueOrNull)
        set(value) {
            valueOrNull = value
        }

    override var valueOrNull: Boolean? = null
        set(value) {
            if (field != value) {
                field = value
                update()
            }
        }

    override fun main(): SwitchField {
        state.update = { update() }
        update()
        return this
    }

    fun update() {
        clear()
        if (config.addLabel) {
            div(displayFlex, alignItemsCenter, justifyContentSpaceBetween) {
                div(labelLarge, pr16) { + state.label }
                switch()
            }
        } else {
            switch()
        }
    }

    fun Z2.switch() {
        div("switch-track".css, selectNone) {
            val selected = (valueOrNull == true)
            val statusClass = (if (selected) "selected" else "unselected").css
            addCss(statusClass)

            if ((valueOrNull == true && config.selectedIcon) || (! selected && config.unselectedIcon)) {
                div("switch-thumb-icon".css, statusClass) {
                    icon(if (selected) browserIcons.switchSelected else browserIcons.switchUnselected, size = 16)
                }
            } else {
                div("switch-thumb".css, statusClass) {}
            }

            onClick {
                valueOrNull = (valueOrNull != true)
                config.onChange?.invoke(this@SwitchField)
            }
        }
    }
}