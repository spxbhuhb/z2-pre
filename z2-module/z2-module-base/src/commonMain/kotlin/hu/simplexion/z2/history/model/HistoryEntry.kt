package hu.simplexion.z2.history.model

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.schematic.runtime.Schematic

class HistoryEntry : Schematic<HistoryEntry>() {

    var id by uuid<HistoryEntry>()

    var createdAt by instant()
    var createdBy by uuid<AccountPrivate>().nullable()

    var flags by int()
    var topic by string()
    var verb by string()
    var subject by uuid<Any>().nullable()

    var contentType by string()
    var textContent by string()

}