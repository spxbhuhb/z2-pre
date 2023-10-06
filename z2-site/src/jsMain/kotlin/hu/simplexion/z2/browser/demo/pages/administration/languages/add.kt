package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.p24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.immaterial.schematic.schematicTextButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.commons.localization.text.LocalizedText
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.localization.model.Language

internal fun add() {
    localLaunch {
        languageModal(Language(), strings.addLanguage, browserStrings.add)
            .show()
            ?.let { /* Languages.add(it) */ }
    }
}

internal fun edit(language: Language) =
    localLaunch {
        languageModal(language, strings.addLanguage, browserStrings.add)
            .show()
            ?.let { /* Languages.update(it) */ }
    }

internal fun languageModal(
    language : Language,
    modalTitle : LocalizedText,
    buttonLabel : LocalizedText
) =

    modal(w400) {
        title(modalTitle)

        grid(p24, gridGap24) {
            field { language.isoCode }
            field { language.countryCode }
            field { language.nativeName }
        }

        buttons {
            textButton(browserStrings.cancel) { closeWith(null) }
            schematicTextButton(language, buttonLabel) { closeWith(language) }
        }
    }