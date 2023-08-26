package hu.simplexion.z2.email.model

import hu.simplexion.z2.schematic.runtime.Schematic

class EmailSettings : Schematic<EmailSettings>() {

    var uuid by uuid<EmailSettings>()

    var enables by boolean()
    var host by string() blank false
    var port by int() min 1 max 65535
    var username by string() blank false minLength 5 maxLength 100
    var password by secret() maxLength 50
    var auth by boolean()
    var tls by boolean()
    var debug by boolean()

}