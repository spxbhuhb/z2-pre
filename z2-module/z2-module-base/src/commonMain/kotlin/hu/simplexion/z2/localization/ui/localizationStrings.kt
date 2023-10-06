package hu.simplexion.z2.localization.ui

import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

object localizationStrings : ILocalizationStrings

interface ILocalizationStrings : LocalizedTextProvider {

    val languages get() = static("Nyelvek")
    val languagesSupport get() = support(languages, "Új nyelv hozzáadása. Címkék és ikonok fordítása.")

    val addLanguage get() = static("Nyelv hozzáadása")
    val editLanguage get() = static("Nyelv szerkesztése")

    val isoCode get() = static("ISO kód")
    val isoCodeSupport get() = support(isoCode, "A nyelv ISO 639 kódja")

    val countryCode get() = static("Ország kód")
    val countryCodeSupport get() = support(countryCode, "Az ország ISO 3166 Alpha-2 kódja")

    val nativeName get() = static("Natív név")
    val nativeNameSupport get() = support(nativeName, "A nyelv neve az adott nyelven.")

}
