package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.runtime.Schematic

class AccountStatus : Schematic<AccountStatus>() {

    val uuid by uuid<AccountStatus>()

    var account by uuid<AccountPrivate>()

    var activated by boolean(default = false)
    var locked by boolean(default = false)
    var expired by boolean(default = false)
    var anonymized by boolean(default = false)
    var lastAuthSuccess by instant().nullable()
    var authSuccessCount by int()
    var lastAuthFail by instant().nullable()
    var authFailCount by int()

}