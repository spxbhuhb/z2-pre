package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.util.UUID

object i18nStrings : LocalizedTextStore(UUID("a5ce25ef-9467-4df5-841e-8945808a7cfd")) {

    val languages by "Nyelvek"
    val languagesSupport by languages.support("Új nyelv hozzáadása. Címkék és ikonok fordítása.")

    val addLanguage by "Nyelv hozzáadása"
    val editLanguage by "Nyelv szerkesztése"

    val isoCode by "ISO kód"
    val isoCodeSupport by isoCode.support("A nyelv ISO 639 kódja")

    val countryCode by "Ország kód"
    val countryCodeSupport by countryCode.support("Az ország ISO 3166 Alpha-2 kódja")

    val nativeName by "Natív név"
    val nativeNameSupport by nativeName.support("A nyelv neve az adott nyelven.")

}
