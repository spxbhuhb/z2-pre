package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class AccountCredentials : Schematic<AccountCredentials>() {
    
    var uuid by uuid<AccountCredentials>()

    var account by uuid<AccountPrivate>()
    var type by string(maxLength = 50, blank = false)
    var value by secret()
    var createdAt by instant()

}