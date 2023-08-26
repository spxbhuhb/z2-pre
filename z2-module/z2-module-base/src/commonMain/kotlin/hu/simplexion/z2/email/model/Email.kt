package hu.simplexion.z2.email.model

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.schematic.runtime.Schematic

class Email : Schematic<Email>() {
    var uuid by uuid<Email>()

    var createdBy by uuid<AccountPrivate>()
    var createdAt by instant()
    var sentAt by instant().nullable()
    var status by enum<EmailStatus>()

    var sensitive by boolean()
    var recipients by string()
    var subject by string() maxLength 200
}