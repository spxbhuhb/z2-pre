package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.runtime.Schematic

class AccountPrivate : Schematic<AccountPrivate>() {

    var uuid by uuid<AccountPrivate>()

    var accountName by string(minLength = 2, maxLength = 50, blank = false)
    var fullName by string(minLength = 5, maxLength = 100, blank = false)
    var email by email()
    var phone by phoneNumber()

}