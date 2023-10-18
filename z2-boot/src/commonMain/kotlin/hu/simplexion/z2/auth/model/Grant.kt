package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class Grant : Schematic<Grant>() {

    var principal by uuid<Principal>()
    var role by uuid<Role>()
    var principalName by string()

}