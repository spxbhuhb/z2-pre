package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.schematic.runtime.Schematic

class Language : Schematic<Language>() {
    val id = uuid<Language>()
    var isoCode by string(minLength = 2, maxLength = 2)
    var countryCode by string(minLength = 2, maxLength = 2, pattern = Regex("[A-Z]{2}"))
    var nativeName by string(minLength = 2, blank = false)
}