package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.runtime.Schematic

class Role : Schematic<Role>() {
    var uuid by uuid<Role>()
    var contextName by string(maxLength = 50).nullable()
    var programmaticName by string(maxLength = 100)
    var displayName by string(maxLength = 50)
}