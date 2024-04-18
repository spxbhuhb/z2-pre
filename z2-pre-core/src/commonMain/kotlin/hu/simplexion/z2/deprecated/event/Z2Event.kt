package hu.simplexion.z2.deprecated.event

import hu.simplexion.z2.util.Z2Handle

interface Z2Event {
    val busHandle: Z2Handle

    fun fire() {
        EventCentral.fire(this)
    }
}