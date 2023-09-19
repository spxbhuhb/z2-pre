package hu.simplexion.z2.content.model

import hu.simplexion.z2.schematic.runtime.Schematic

class ContentType : Schematic<ContentType>() {
    var uuid by uuid<ContentType>()
    var extension by string() blank false minLength 1 maxLength 20
    var mimeType by string() maxLength 100
    var sizeLimit by long(5L*1024L*1024L) min 0
    var allowed by boolean(true)
}