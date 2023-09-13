package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.commons.i18n.LocalizedTextStore

internal object strings : LocalizedTextStore() {

    val languages by "Languages"
    val languagesSupport by languages.support("Register new languages. Translate user interface labels and icons.")

    val addLanguage by "Add Language"

    val isoCode by "ISO Code"
    val isoCodeSupport by isoCode.support("ISO 639 code of the language")

    val countryCode by "Country Code"
    val countryCodeSupport by countryCode.support("ISO 3166 Alpha-2 code of the language")

    val nativeName by "Native Name"
    val nativeNameSupport by nativeName.support("Name of the language in the language itself")

}
