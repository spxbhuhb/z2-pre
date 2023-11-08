package hu.simplexion.z2.browser.field.stereotype

import hu.simplexion.z2.browser.css.textAlignEnd
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.localization.locales.localized
import hu.simplexion.z2.localization.locales.localizedFormats
import kotlinx.datetime.Instant
import kotlinx.dom.addClass

class InstantField(
    parent: Z2,
    state: FieldState = FieldState(),
    config: FieldConfig<Instant>
) : AbstractField<Instant>(
    parent, state, config
) {
    override fun main(): InstantField {
        super.main()
        config.encodeToString = { it.localized }
        config.decodeFromString = { localizedFormats.toInstant(it) } // FIXME instant decode from string (+ null)
        inputElement.addClass(textAlignEnd)
        return this
    }
}