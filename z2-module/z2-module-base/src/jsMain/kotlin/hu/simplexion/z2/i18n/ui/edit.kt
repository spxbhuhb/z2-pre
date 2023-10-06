package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.p24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.commons.localization.text.LocalizedText
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.i18n.languages
import hu.simplexion.z2.localization.model.Language
import hu.simplexion.z2.localization.ui.localizationStrings

internal fun add() =
    languageModal(Language(), localizationStrings.addLanguage, browserStrings.add) { languages.add(it) }

internal fun edit(language: Language) =
    languageModal(language, localizationStrings.editLanguage, browserStrings.edit) { languages.update(it) }

internal fun languageModal(
    language: Language,
    modalTitle: LocalizedText,
    buttonLabel: LocalizedText,
    onOk: suspend (language: Language) -> Unit
) {
    localLaunch {
        modal(w400) {
            title(modalTitle)

            grid(p24, gridGap24) {
                field { language.isoCode }
                field { language.countryCode }
                field { language.nativeName }
            }

            buttons {
                textButton(browserStrings.cancel) { closeWith(false) }
                textButton(buttonLabel) {
                    localLaunch {
                        onOk(language)
                        closeWith(true)
                    }
                }
            }
        }.show()
    }
}