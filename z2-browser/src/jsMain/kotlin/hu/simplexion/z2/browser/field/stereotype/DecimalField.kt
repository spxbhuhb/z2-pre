package hu.simplexion.z2.browser.field.stereotype

import hu.simplexion.z2.browser.css.textAlignEnd
import hu.simplexion.z2.browser.css.w304
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.localization.locales.AbstractLocalizedFormats
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
    override var valueOrNull: Long? = null
        set(value) {
            if (field != value && value != null) {
                inputElement.value = config.encodeToString(value)
                update()
            }
            field = value

        }

    override fun main(): DecimalField {
        config.encodeToString = {
            it.toDecimalString(scale)
        }

        config.decodeFromString = {
            val double = localizedFormats.toDoubleOrNull(it)
            if (double == null) {
                0L
            } else {
                (double * AbstractLocalizedFormats.shifts[scale]).toLong()
            }
        }

        super.main()

        addClass(w304)
        inputElement.addClass(textAlignEnd)

        return this
    }
}