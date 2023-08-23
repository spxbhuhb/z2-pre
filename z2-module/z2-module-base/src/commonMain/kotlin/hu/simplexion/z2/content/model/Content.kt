package hu.simplexion.z2.content.model

import hu.simplexion.z2.schematic.runtime.Schematic

class Content : Schematic<Content>() {
    var uuid by uuid<Content>()
    var name by string(blank = false, maxLength = 200) // , pattern = Regex("[\\p{Alpha}\\d ._!()\\-,%]{1,200}")
    var type by string(maxLength = 40)
    var size by long(min = 0)
    var sha256 by string(maxLength = 44).nullable()
    var status by enum(ContentStatus.entries.toTypedArray())
}