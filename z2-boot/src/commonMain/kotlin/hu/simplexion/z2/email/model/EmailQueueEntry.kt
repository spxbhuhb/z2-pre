package hu.simplexion.z2.email.model

import hu.simplexion.z2.schematic.runtime.Schematic

class EmailQueueEntry : Schematic<EmailQueueEntry>() {

    var email by uuid<Email>()
    var tries by int()
    var lastTry by instant().nullable()
    var lastFailMessage by string().nullable()

}