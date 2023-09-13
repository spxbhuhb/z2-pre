package hu.simplexion.z2.browser.field.stereotypes

import hu.simplexion.z2.browser.css.textAlignEnd
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import kotlinx.dom.addClass

class IntField(
    parent: Z2,
    value : Int,
    state: FieldState = FieldState(),
    config: FieldConfig<Int>
) : AbstractField<Int>(
    parent, state, config
) {
    override var value: Int
        get() = inputElement.value.toIntOrNull() ?: 0
        set(value) {
            inputElement.value = value.toString()
        }

    init {
        inputElement.addClass(textAlignEnd)
        this.value = value
    }
}