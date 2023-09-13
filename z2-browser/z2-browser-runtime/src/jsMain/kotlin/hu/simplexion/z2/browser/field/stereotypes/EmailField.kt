package hu.simplexion.z2.browser.field.stereotypes

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig

class EmailField(
    parent: Z2,
    value : String,
    state: FieldState = FieldState(),
    config: FieldConfig<String>
) : AbstractField<String>(
    parent, state, config
) {
    override var value
        get() = inputElement.value
        set(value) {
            inputElement.value = value
        }

    init {
        inputElement.type = "email"
        this.value = value
    }
}