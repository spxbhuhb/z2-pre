package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class SecurityPolicy : Schematic<SecurityPolicy>() {
    val uuid by uuid<SecurityPolicy>()

    var passwordChangeInterval by duration()
    val passwordHistoryLength by int()
    val passwordLengthMinimum by int()
    val uppercaseMinimum by int()
    val digitMinimum by int()
    val specialCharacterMinimum by int()
    val sameCharacterMaximum by int()
    val minEntropy by int()
    val maxFailedAuths by int(default = 5)

}