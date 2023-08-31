package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.p24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.components.schematic.field
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.browser.components.schematic.schematicTextButton
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.localLaunch

internal fun add() {
    localLaunch {
        languageModal(Language(), strings.addLanguage, basicStrings.add)
            .show()
            ?.let { /* Languages.add(it) */ }
    }
}

internal fun edit(language: Language) =
    localLaunch {
        languageModal(language, strings.addLanguage, basicStrings.add)
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
            textButton(basicStrings.cancel) { closeWith(null) }
            schematicTextButton(language, buttonLabel) { closeWith(language) }
        }
    }