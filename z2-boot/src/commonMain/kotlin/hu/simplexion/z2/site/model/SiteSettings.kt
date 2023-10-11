package hu.simplexion.z2.site.model

import hu.simplexion.z2.schematic.runtime.Schematic

class SiteSettings : Schematic<SiteSettings>() {

    var protocol by string()
    var host by string()
    var port by int() min 1 max 65535

    var name by string()

    var test by boolean() default true
    var testPassword by secret()

}