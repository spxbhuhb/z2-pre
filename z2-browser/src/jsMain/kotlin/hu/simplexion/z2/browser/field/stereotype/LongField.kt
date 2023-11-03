package hu.simplexion.z2.browser.field.stereotype

import hu.simplexion.z2.browser.css.textAlignEnd
import hu.simplexion.z2.browser.css.w304
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.onBlur
import hu.simplexion.z2.browser.html.onFocus
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import kotlinx.dom.addClass

class LongField(
    parent: Z2,
    state: FieldState = FieldState(),
    config: FieldConfig<Long>
) : AbstractField<Long>(
    parent, state, config
) {
    override fun main(): LongField {
        super.main()

        input.onFocus {
            if (! state.touched && valueOrNull == 0L) {
                inputElement.value = ""
            }
        }

        input.onBlur {
            if (! state.touched && valueOrNull == 0L) {
                value = 0L // to refresh the field
            }
        }

        addClass(w304)
        inputElement.addClass(textAlignEnd)
        inputElement.type = "number"
        return this
    }
}