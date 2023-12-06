package hu.simplexion.z2.auth.model

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.service.ServiceContext

class Session : Schematic<Session>() {

    var uuid by uuid<ServiceContext>()
    var securityCode by string()
    val createdAt by instant()
    var lastActivity by instant()
    var principal by uuid<Principal>().nullable()
    var roles by schematicList<Role>()

    companion object {
        val SESSION_TOKEN_UUID = UUID<Session>("7fdd494f-e542-4d5b-870b-7cab83dc3197")
        val LOGOUT_TOKEN_UUID = UUID<Session>("61e974dc-094b-42e8-b21c-08502be7c595")
    }
}