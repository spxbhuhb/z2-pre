package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class SecurityPolicy : Schematic<SecurityPolicy>() {
    var twoFactorAuthentication by boolean()
    var sessionActivationInterval by int(5) // minutes
    var sessionExpirationInterval by int(30) // minutes
    var passwordChangeInterval by int() // days
    var passwordHistoryLength by int()
    var passwordLengthMinimum by int()
    var uppercaseMinimum by int()
    var digitMinimum by int()
    var specialCharacterMinimum by int()
    var sameCharacterMaximum by int()
    var minEntropy by enum<EntropyCategory>()
    var maxFailedAuths by int(default = 5)
}