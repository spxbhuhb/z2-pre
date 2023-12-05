package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class SecurityPolicy : Schematic<SecurityPolicy>() {
    var passwordChangeInterval by int()
    var passwordHistoryLength by int()
    var passwordLengthMinimum by int()
    var uppercaseMinimum by int()
    var digitMinimum by int()
    var specialCharacterMinimum by int()
    var sameCharacterMaximum by int()
    var minEntropy by enum<EntropyCategory>()
    var maxFailedAuths by int(default = 5)
}