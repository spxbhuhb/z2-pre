package hu.simplexion.z2.email.model

import hu.simplexion.z2.schematic.runtime.Schematic

class EmailSettings : Schematic<EmailSettings>() {

    var uuid by uuid<EmailSettings>()

    var host by string() blank false
    var port by int() min 1 max 65535
    var username by string() blank false minLength 5 maxLength 100
    var password by secret() maxLength 50
    var protocol by string()
    var auth by boolean()
    var tls by boolean()
    var debug by boolean()

    var retryCheckInterval by long(300_000) // FIXME this should be a duration, but it fails for some reason
}