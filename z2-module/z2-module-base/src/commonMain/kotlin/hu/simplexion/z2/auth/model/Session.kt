package hu.simplexion.z2.auth.model

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.Schematic

class Session : Schematic<Session>() {

    val uuid by uuid<Session>()
    val createdAt by instant()
    var account by uuid <AccountPrivate>().nullable()
    var roles by string() // FIXME session.roles should be a list
    var tokens by string() // FIXME session.tokens shuld be a list
    var fullName by string()

    companion object {
        val SESSION_TOKEN_UUID = UUID<Session>("7fdd494f-e542-4d5b-870b-7cab83dc3197")
    }
}