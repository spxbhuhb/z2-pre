package hu.simplexion.z2.email.model

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.schematic.Schematic

class EmailQuery : Schematic<EmailQuery>() {
    var sender by uuid<Principal>().nullable()
    var recipient by string().nullable()
    var after by instant().nullable()
    var before by instant().nullable()
    var subject by string().nullable()
    var hasAttachment by boolean().nullable()
    var status by enum<EmailStatus>().nullable()
    var limit by int(default = 100, max = 10000)
    var offset by long(default = 0)
}