package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.css.alignItemsCenter
import hu.simplexion.z2.browser.css.cursorPointer
import hu.simplexion.z2.browser.css.displayFlex
import hu.simplexion.z2.browser.css.pl8
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.layout.scrolledBoxWithLabel
import hu.simplexion.z2.localization.runtime.localized

class RadioButtonGroup<T>(
    parent: Z2,
    override val state : FieldState,
    val config : RadioGroupConfig<T>
) : Z2(parent), ValueField<T> {

    val buttons = mutableListOf<RadioButtonBase>()

    override var value : T
        get() = checkNotNull(valueOrNull)
        set(value) {
            valueOrNull = value
        }

    override var valueOrNull : T? = null
        set(value) {
            field = value
            for (index in config.options.indices) {
                buttons[index].selected = (value == config.options[index])
            }
        }

    init {
        state.update = { update() }
        config.update = { update() }
        update()
    }

    fun onChange(entry: T) {
        valueOrNull = entry
        config.onChange?.invoke(this, entry)
    }

    fun update() {
        clear()
        scrolledBoxWithLabel((state.label ?: "").localized, border = (config.style == FieldStyle.Outlined)) {
            renderItems()
        }
    }

    fun Z2.renderItems() {
        for (entry in config.options) {
            div(displayFlex, alignItemsCenter) {
                buttons += radioButton(entry == valueOrNull, false) { onChange(entry) }
                div(pl8, cursorPointer) {
                    config.itemBuilderFun(this, entry)
                    onClick { if (! state.readOnly) onChange(entry) }
                }
            }
        }
    }
}