package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.runtime.Schematic

class AccountStatus : Schematic<AccountStatus>() {

    val uuid by uuid<AccountStatus>()

    var account by uuid<AccountPrivate>()

    var validated by boolean(default = false)
    var locked by boolean(default = true)
    var expired by boolean(default = false)
    var anonymized by boolean(default = false)
    var lastLoginSuccess by instant().nullable()
    var loginSuccessCount by int()
    var lastLoginFail by instant().nullable()
    var loginFailCount by int()

}