package hu.simplexion.z2.history.model

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion

class HistoryEntry : Schematic<HistoryEntry>() {

    var id by uuid<HistoryEntry>()

    var createdAt by instant()
    var createdBy by uuid<AccountPrivate>().nullable()
    var createdFor by uuid<Any>().nullable()

    var flags by int()

    var contentType by string()
    var content by string()
}