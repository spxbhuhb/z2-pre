package hu.simplexion.z2.localization.ui

import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.p24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.localization.localeService
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.text.LocalizedText

internal fun add() =
    languageModal(Locale(), baseStrings.addLocale, browserStrings.add) { localeService.add(it) }

internal fun edit(locale: Locale) =
    languageModal(locale, baseStrings.editLanguage, browserStrings.edit) { localeService.update(it) }

internal fun languageModal(
    locale: Locale,
    modalTitle: LocalizedText,
    buttonLabel: LocalizedText,
    onOk: suspend (locale: Locale) -> Unit
) {
    localLaunch {
        modal(w400) {
            title(modalTitle)

            grid(p24, gridGap24) {
                field { locale.isoCode }
                field { locale.countryCode }
                field { locale.nativeName }
            }

            buttons {
                textButton(browserStrings.cancel) { closeWith(false) }
                textButton(buttonLabel) {
                    localLaunch {
                        onOk(locale)
                        closeWith(true)
                    }
                }
            }
        }.show()
    }
}