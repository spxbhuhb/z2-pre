package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.runtime.Schematic

class AccountPublic : Schematic<AccountPublic>() {

    var uuid by uuid<AccountPublic>()

    var fullName by string(minLength = 5, maxLength = 100, blank = false)

}