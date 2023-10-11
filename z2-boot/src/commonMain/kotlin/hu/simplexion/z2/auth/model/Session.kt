package hu.simplexion.z2.auth.model

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.service.runtime.ServiceContext

class Session : Schematic<Session>() {

    var uuid by uuid<ServiceContext>()
    val createdAt by instant()
    var account by uuid<AccountPrivate>().nullable()
    var roles by schematicList<Role>()
    var fullName by string()

    companion object {
        val SESSION_TOKEN_UUID = UUID<Session>("7fdd494f-e542-4d5b-870b-7cab83dc3197")
    }
}