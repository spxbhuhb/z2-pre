package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Z2Handle

interface Z2Event {
    val busHandle: Z2Handle

    fun fire() {
        EventCentral.fire(this)
    }
}