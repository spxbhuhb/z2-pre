package hu.simplexion.z2.auth.model

import hu.simplexion.z2.schematic.Schematic

class AccountSecret : Schematic<AccountSecret>() {
    val secret by secret()
}