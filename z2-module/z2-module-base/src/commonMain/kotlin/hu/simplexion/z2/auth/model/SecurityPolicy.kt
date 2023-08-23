package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.runtime.Schematic

class SecurityPolicy : Schematic<SecurityPolicy>() {
    val id by uuid<SecurityPolicy>()

    var passwordChangeInterval by duration()
    val passwordHistoryLength by int()
    val passwordLengthMinimum by int()
    val uppercaseMinimum by int()
    val digitMinimum by int()
    val specialCharacterMinimum by int()
    val sameCharacterMaximum by int()
    val minEntropy by int()
    val maxFailedLogins by int(default = 5)

}