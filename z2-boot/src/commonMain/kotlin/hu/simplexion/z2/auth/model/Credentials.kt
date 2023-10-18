package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class Credentials : Schematic<Credentials>() {
    
    var uuid by uuid<Credentials>()

    var principal by uuid<Principal>()
    var type by string(maxLength = 50, blank = false)
    var value by secret()
    var createdAt by instant()

}