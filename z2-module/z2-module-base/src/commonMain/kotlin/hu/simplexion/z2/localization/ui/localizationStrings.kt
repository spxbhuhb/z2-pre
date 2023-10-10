package hu.simplexion.z2.localization.ui

import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

object localizationStrings : ILocalizationStrings

interface ILocalizationStrings : LocalizedTextProvider {

    val languages get() = static("Nyelvek", "Új nyelv hozzáadása. Címkék és ikonok fordítása.")
    val addLanguage get() = static("Nyelv hozzáadása")
    val editLanguage get() = static("Nyelv szerkesztése")
    val isoCode get() = static("ISO kód", "A nyelv ISO 639 kódja")
    val countryCode get() = static("Ország kód", "Az ország ISO 3166 Alpha-2 kódja")
    val nativeName get() = static("Natív név", "A nyelv neve az adott nyelven.")

}
