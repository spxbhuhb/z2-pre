package hu.simplexion.z2.alarm.model

import hu.simplexion.z2.schematic.runtime.Schematic

class Alarm : Schematic<Alarm>() {

    var uuid by uuid<Alarm>()
    var createdAt by instant()

}