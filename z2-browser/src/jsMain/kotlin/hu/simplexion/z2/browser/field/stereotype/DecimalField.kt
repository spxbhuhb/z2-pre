package hu.simplexion.z2.browser.field.stereotype

import hu.simplexion.z2.browser.css.textAlignEnd
import hu.simplexion.z2.browser.css.w304
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.onBlur
import hu.simplexion.z2.browser.html.onFocus
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.localization.locales.AbstractLocalizedFormats
import hu.simplexion.z2.localization.locales.localized
import hu.simplexion.z2.localization.locales.localizedFormats
import hu.simplexion.z2.localization.locales.toDecimalString
import kotlinx.dom.addClass

class DecimalField(
    parent: Z2,
    state: FieldState = FieldState(),
    config: FieldConfig<Long>,
    val scale: Int
) : AbstractField<Long>(
    parent, state, config
) {

    override fun main(): DecimalField {

        config.encodeToString = {
            if (it == 0L) "0" else it.toDecimalString(scale).removeSurrounding("0")
        }

        config.decodeFromString = {
            if (it.isEmpty()) {
                0L
            } else {
                (localizedFormats.toDouble(it) * AbstractLocalizedFormats.shifts[scale]).toLong()
            }
        }

        super.main()

        addClass(w304)
        inputElement.addClass(textAlignEnd)

        input.onFocus {
            if (! state.touched && valueOrNull == 0L) {
                inputElement.value = ""
            }
        }

        input.onBlur {
            if (! state.touched && valueOrNull == 0L) {
                value = 0L // to refresh the field
            }
            if (value != 0L) {
                inputElement.value = value.localized
            }
        }

        return this
    }

}