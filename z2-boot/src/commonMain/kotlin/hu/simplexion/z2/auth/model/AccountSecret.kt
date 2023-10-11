package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.runtime.Schematic

class AccountSecret : Schematic<AccountSecret>() {
    val secret by secret()
}