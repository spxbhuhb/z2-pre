package hu.simplexion.z2.localization.model

import hu.simplexion.z2.schematic.Schematic

class Locale : Schematic<Locale>() {
    var uuid by uuid<Locale>()
    var isoCode by string() minLength 2 maxLength 2
    var countryCode by string() minLength 2 maxLength 2 pattern Regex("[A-Z]{2}")
    var nativeName by string() minLength 2 maxLength 30 blank false
    var priority by int()
    var visible by boolean() default false
}