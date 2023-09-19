package hu.simplexion.z2.browser.field.stereotypes

import hu.simplexion.z2.browser.css.textAlignEnd
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import kotlinx.dom.addClass

class IntField(
    parent: Z2,
    state: FieldState = FieldState(),
    config: FieldConfig<Int>
) : AbstractField<Int>(
    parent, state, config
) {
    override fun main(): IntField {
        super.main()
        inputElement.addClass(textAlignEnd)
        return this
    }
}