package hu.simplexion.z2.history.model

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.schematic.Schematic

class HistoryEntry : Schematic<HistoryEntry>() {

    var uuid by uuid<HistoryEntry>()

    var createdAt by instant()
    var createdBy by uuid<Principal>().nullable()

    var flags by int()
    var topic by string()
    var verb by string()
    var subject by uuid<Any>().nullable()

    var contentType by string()
    var textContent by string()

}