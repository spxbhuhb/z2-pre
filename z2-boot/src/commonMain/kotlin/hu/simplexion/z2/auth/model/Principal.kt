package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class Principal : Schematic<Principal>() {

    var uuid by uuid<Principal>(true)

    var name by string(blank = false)
    var activated by boolean(default = false)
    var locked by boolean(default = false)
    var expired by boolean(default = false)
    var anonymized by boolean(default = false)
    var lastAuthSuccess by instant().nullable()
    var authSuccessCount by int()
    var lastAuthFail by instant().nullable()
    var authFailCount by int()

}