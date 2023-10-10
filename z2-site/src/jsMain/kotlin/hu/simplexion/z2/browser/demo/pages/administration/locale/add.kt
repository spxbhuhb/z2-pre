package hu.simplexion.z2.browser.demo.pages.administration.locale

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
import hu.simplexion.z2.localization.localeService
import hu.simplexion.z2.localization.model.Locale

internal fun add() {
    localLaunch {
        languageModal(Locale(), strings.addLocale, browserStrings.add)
            .show()
            ?.let { localeService.add(it) }
    }
}

internal fun edit(locale: Locale) =
    localLaunch {
        languageModal(locale, strings.addLocale, browserStrings.add)
            .show()
            ?.let { localeService.update(it) }
    }

internal fun languageModal(
    locale : Locale,
    modalTitle : LocalizedText,
    buttonLabel : LocalizedText
) =

    modal(w400) {
        title(modalTitle)

        grid(p24, gridGap24) {
            field { locale.isoCode }
            field { locale.countryCode }
            field { locale.nativeName }
        }

        buttons {
            textButton(browserStrings.cancel) { closeWith(null) }
            schematicTextButton(locale, buttonLabel) { closeWith(locale) }
        }
    }